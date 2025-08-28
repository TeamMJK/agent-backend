package team.mjk.agent.domain.receipt.application;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.model.Media;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeType;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.company.domain.CompanyRepository;
import team.mjk.agent.domain.company.domain.Workspace;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.member.presentation.exception.MemberNotFoundException;
import team.mjk.agent.domain.receipt.domain.Receipt;
import team.mjk.agent.domain.receipt.domain.ReceiptRepository;
import team.mjk.agent.domain.receipt.dto.request.ReceiptMcpRequest;
import team.mjk.agent.domain.receipt.dto.request.ReceiptSaveRequest;
import team.mjk.agent.domain.receipt.dto.response.ReceiptGetResponse;
import team.mjk.agent.domain.receipt.dto.response.ReceiptSaveResponse;
import team.mjk.agent.domain.receipt.presentation.exception.*;
import team.mjk.agent.global.mcp.McpService;
import team.mjk.agent.global.mcp.McpServiceRegistry;

@RequiredArgsConstructor
@Service
public class ReceiptService {

  private final S3Client s3Client;
  private final ReceiptRepository receiptRepository;
  private final MemberRepository memberRepository;
  private final ChatClient chatClient;
  private final McpServiceRegistry registry;

  @Value("${cloud.aws.cloudfront.domain}")
  private String cloudFrontDomain;

  @Value("${cloud.aws.s3.bucketName}")
  private String bucketName;

  private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif");

  @Transactional
  public ReceiptSaveResponse saveReceipt(Long memberId, ReceiptSaveRequest request) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getCompany();

    Receipt receipt = Receipt.builder()
            .member(member)
            .paymentDate(request.paymentDate())
            .approvalNumber(request.approvalNumber())
            .storeAddress(request.storeAddress())
            .totalAmount(request.totalAmount())
            .company(company)
            .build();

    receiptRepository.save(receipt);

    return ReceiptSaveResponse.builder()
            .receiptId(receipt.getId())
            .build();
  }

  @Transactional
  public String upload(Long memberId, MultipartFile image) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getCompany();

    if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
      throw new EmptyFileExceptionCode();
    }

    String imageUrl = uploadImage(image);

    Receipt receipt = Receipt.builder()
            .member(member)
            .company(company)
            .url(imageUrl)
            .build();

    receiptRepository.save(receipt);

    return imageUrl;
  }

  @Transactional
  public ReceiptSaveResponse saveOcrInfo(Long memberId, MultipartFile file) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getCompany();

    String imageUrl = uploadImage(file);
    ReceiptSaveRequest request = ocr(file);

    Receipt receipt = Receipt.builder()
            .member(member)
            .paymentDate(request.paymentDate())
            .approvalNumber(request.approvalNumber())
            .storeAddress(request.storeAddress())
            .totalAmount(request.totalAmount())
            .company(company)
            .url(imageUrl)
            .build();

    receiptRepository.save(receipt);

    return ReceiptSaveResponse.builder()
            .receiptId(receipt.getId())
            .build();
  }

  @Transactional
  public Workspace saveMcp(Long memberId, MultipartFile file) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getCompany();

    Workspace workspace = company.getWorkspace();
    if (workspace == Workspace.NONE) {
      saveOcrInfo(memberId, file);
      return workspace;
    }

    String imageUrl = uploadImage(file);
    ReceiptSaveRequest request = ocr(file);

    ReceiptMcpRequest mcpRequest = ReceiptMcpRequest.builder()
            .name(member.getName())
            .approvalNumber(request.approvalNumber())
            .storeAddress(request.storeAddress())
            .totalAmount(request.totalAmount())
            .imageUrl(imageUrl)
            .paymentDate(request.paymentDate())
            .build();

    McpService mcpService = registry.getService(workspace);
    mcpService.createReceipt(mcpRequest, company.getId());

    return company.getWorkspace();
  }

  public void deleteImageFromS3(Long memberId, String imageAddress) {
    Receipt receipt = receiptRepository.findByUrl(imageAddress);
    validateForbidden(memberId, receipt.getMember().getId());

    String key = getKeyFromImageAddress(imageAddress);
    try {
      s3Client.deleteObject(DeleteObjectRequest.builder()
              .bucket(bucketName)
              .key(key)
              .build());
    } catch (Exception e) {
      throw new OnImageDeleteExceptionCode();
    }
  }

  @Transactional(readOnly = true)
  public List<ReceiptGetResponse> getAllReceipt(Long memberId) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getCompany();

    return receiptRepository.findAllByCompany(company).stream()
            .map(receipt -> ReceiptGetResponse.builder()
                    .receiptId(receipt.getId())
                    .approvalNumber(receipt.getApprovalNumber())
                    .paymentDate(receipt.getPaymentDate())
                    .storeAddress(receipt.getStoreAddress())
                    .totalAmount(receipt.getTotalAmount())
                    .build())
            .toList();
  }

  @Transactional(readOnly = true)
  public ReceiptGetResponse getReceipt(Long memberId, Long receiptId) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getCompany();
    Receipt receipt = receiptRepository.findByIdAndCompany(receiptId, company);

    return ReceiptGetResponse.builder()
            .receiptId(receipt.getId())
            .approvalNumber(receipt.getApprovalNumber())
            .paymentDate(receipt.getPaymentDate())
            .storeAddress(receipt.getStoreAddress())
            .totalAmount(receipt.getTotalAmount())
            .build();
  }

  @Transactional
  public void deleteReceipt(Long memberId, Long receiptId) {
    Receipt receipt = receiptRepository.findByReceiptId(receiptId);
    validateForbidden(memberId, receipt.getMember().getId());
    receiptRepository.delete(receipt);
  }

  private String uploadImage(MultipartFile image) {
    validateImageFileExtension(image.getOriginalFilename());
    try {
      String s3FileName = uploadImageToS3(image);
      return cloudFrontDomain + "/" + s3FileName;
    } catch (IOException e) {
      throw new OnImageUploadExceptionCode();
    }
  }

  private String uploadImageToS3(MultipartFile image) throws IOException {
    String originalFilename = image.getOriginalFilename();
    String extension = Objects.requireNonNull(originalFilename)
            .substring(originalFilename.lastIndexOf(".") + 1)
            .toLowerCase();

    String s3FileName = UUID.randomUUID().toString().substring(0, 10) + "_" +
            originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_");

    try (InputStream is = image.getInputStream()) {
      PutObjectRequest putObjectRequest = PutObjectRequest.builder()
              .bucket(bucketName)
              .key(s3FileName)
              .contentType("image/" + extension)
              .build();

      s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(is, image.getSize()));
    } catch (Exception e) {
      throw new PutObjectExceptionCode();
    }

    return s3FileName;
  }

  private ReceiptSaveRequest ocr(MultipartFile image) {
    String imageUrl = uploadImage(image);
    String key = getKeyFromImageAddress(imageUrl);

    ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(
            GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build()
    );

    ByteArrayResource imageResource = new ByteArrayResource(objectBytes.asByteArray());
    Media media = new Media(MimeType.valueOf(MediaType.IMAGE_JPEG_VALUE), imageResource);

    String fullPrompt = """
            다음 사진에서 영수증 정보를 추출해줘.
            paymentDate, approvalNumber, storeAddress, totalAmount
            """;

    return chatClient.prompt()
            .user(p -> p.text(fullPrompt).media(media))
            .call()
            .entity(ReceiptSaveRequest.class);
  }

  private String getKeyFromImageAddress(String imageAddress) {
    try {
      URL url = new URL(imageAddress);
      return URLDecoder.decode(url.getPath().substring(1), UTF_8);
    } catch (MalformedURLException e) {
      throw new OnImageDeleteExceptionCode();
    }
  }

  private void validateImageFileExtension(String filename) {
    String extension = extractExtension(filename).orElseThrow(NoFileExtensionExceptionCode::new);
    if (!ALLOWED_EXTENSIONS.contains(extension)) {
      throw new InvalidFileExtensionExceptionCode();
    }
  }

  private Optional<String> extractExtension(String filename) {
    int lastIndexOf = filename.lastIndexOf(".");
    if (lastIndexOf == -1 || lastIndexOf == filename.length() - 1) {
      return Optional.empty();
    }
    return Optional.of(filename.substring(lastIndexOf + 1).toLowerCase());
  }

  private void validateForbidden(Long memberId, Long receiptMemberId) {
    if (!receiptMemberId.equals(memberId)) {
      throw new DeleteNotForbiddenExceptionCode();
    }
  }

}
