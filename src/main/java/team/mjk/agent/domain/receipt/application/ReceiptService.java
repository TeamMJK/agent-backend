package team.mjk.agent.domain.receipt.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.member.presentation.exception.MemberNotFoundException;
import team.mjk.agent.domain.receipt.domain.Receipt;
import team.mjk.agent.domain.receipt.domain.ReceiptRepository;
import team.mjk.agent.domain.receipt.dto.request.ReceiptSaveRequest;
import team.mjk.agent.domain.receipt.dto.response.ReceiptGetResponse;
import team.mjk.agent.domain.receipt.dto.response.ReceiptSaveResponse;
import team.mjk.agent.domain.receipt.presentation.exception.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

@RequiredArgsConstructor
@Service
public class ReceiptService {

    private final AmazonS3 amazonS3;
    private final ReceiptRepository receiptRepository;
    private final MemberRepository memberRepository;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif");

    @Transactional
    public ReceiptSaveResponse saveReceipt(Long memberId, ReceiptSaveRequest request) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(MemberNotFoundException::new);

        Receipt receipt = Receipt.builder()
                .member(member)
                .paymentDate(request.paymentDate())
                .approvalNumber(request.approvalNumber())
                .storeAddress(request.storeAddress())
                .totalAmount(request.totalAmount())
                .company(member.getCompany())
                .build();

        receiptRepository.save(receipt);

        return ReceiptSaveResponse.builder()
                .receiptId(receipt.getId())
                .build();
    }

    @Transactional
    public String upload(Long memberId, MultipartFile image) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(MemberNotFoundException::new);
        if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
            throw new EmptyFileExceptionCode();
        }
        String imageUrl = this.uploadImage(image);

        Receipt receipt = Receipt.builder()
                .member(member)
                .url(imageUrl)
                .build();

        receiptRepository.save(receipt);

        return imageUrl;
    }

    public void deleteImageFromS3(Long memberId, String imageAddress) {
        Receipt receipt = receiptRepository.findByUrl(imageAddress);

        Long receiptId = receipt.getId();
        validateForbidden(memberId, receiptId);

        String key = getKeyFromImageAddress(imageAddress);
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        } catch (Exception e) {
            throw new OnImageDeleteExceptionCode();
        }
    }

    @Transactional(readOnly = true)
    public List<ReceiptGetResponse> getAllReceipt(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(MemberNotFoundException::new);

        Company company = member.getCompany();

        List<Receipt> receiptList = receiptRepository.findAllByCompany(company);

        return receiptList.stream()
                .map(receipt -> ReceiptGetResponse.builder()
                        .receiptId(receipt.getId())
                        .approvalNumber(receipt.getApprovalNumber())
                        .paymentDate(receipt.getPaymentDate())
                        .storeAddress(receipt.getStoreAddress())
                        .totalAmount(receipt.getTotalAmount())
                        .build())
                .toList();
    }

    @Transactional
    public void deleteReceipt(Long memberId, Long receiptId) {
        Receipt receipt = receiptRepository.findByReceiptId(receiptId);

        Long receiptMemberId = receipt.getMember().getId();
        validateForbidden(memberId, receiptMemberId);

        receiptRepository.delete(receipt);
    }

    private String uploadImage(MultipartFile image) {
        this.validateImageFileExtension(image.getOriginalFilename());
        try {
            return this.uploadImageToS3(image);
        } catch (IOException e) {
            throw new OnImageUploadExceptionCode();
        }
    }

    private void validateImageFileExtension(String filename) {
        String extension = extractExtension(filename)
                .orElseThrow(NoFileExtensionExceptionCode::new);

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

    private String uploadImageToS3(MultipartFile image) throws IOException {
        String originalFilename = image.getOriginalFilename();
        String extension = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf("."));

        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename;

        InputStream is = image.getInputStream();
        byte[] bytes = IOUtils.toByteArray(is);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + extension);
        metadata.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putObjectRequest);
        } catch (Exception e) {
            throw new PutObjectExceptionCode();
        } finally {
            byteArrayInputStream.close();
            is.close();
        }

        return amazonS3.getUrl(bucketName, s3FileName).toString();
    }

    @Transactional(readOnly = true)
    public ReceiptGetResponse getReceipt(Long memberId, Long receiptId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(MemberNotFoundException::new);

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

    private String getKeyFromImageAddress(String imageAddress) {
        try {
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), UTF_8);
            return decodingKey.substring(1);
        } catch (MalformedURLException e) {
            throw new OnImageDeleteExceptionCode();
        }
    }

    private void validateForbidden(Long memberId, Long receiptMemberId) {
        if (!receiptMemberId.equals(memberId)) {
            throw new DeleteNotForbiddenExceptionCode();
        }
    }

}