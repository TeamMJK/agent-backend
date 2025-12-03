package team.mjk.agent.domain.receipt.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.model.Media;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeType;
import org.springframework.web.multipart.MultipartFile;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.company.domain.CompanyRepository;
import team.mjk.agent.domain.company.domain.Workspace;
import team.mjk.agent.domain.mcp.McpService;
import team.mjk.agent.domain.mcp.McpServiceRegistry;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.receipt.application.dto.request.ReceiptSaveServiceRequest;
import team.mjk.agent.domain.receipt.application.dto.request.ReceiptUpdateServiceRequest;
import team.mjk.agent.domain.receipt.domain.Receipt;
import team.mjk.agent.domain.receipt.domain.ReceiptRepository;
import team.mjk.agent.domain.receipt.dto.request.ReceiptMcpRequest;
import team.mjk.agent.domain.receipt.application.dto.response.ImageUploadResponse;
import team.mjk.agent.domain.receipt.application.dto.response.ReceiptSaveResponse;
import team.mjk.agent.domain.receipt.application.dto.response.ReceiptUpdateResponse;
import team.mjk.agent.domain.receipt.presentation.exception.DeleteNotForbiddenExceptionCode;
import team.mjk.agent.domain.receipt.presentation.exception.EmptyFileExceptionCode;
import team.mjk.agent.domain.receipt.presentation.request.ReceiptSaveRequest;
import team.mjk.agent.global.s3.S3Provider;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ReceiptCommandService {

    private final ReceiptRepository receiptRepository;
    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final ChatClient chatClient;
    private final McpServiceRegistry registry;
    private final S3Provider s3Provider;

    @Transactional
    public ReceiptSaveResponse saveReceipt(
            ReceiptSaveServiceRequest request,
            MultipartFile image
    ) {
        Member member = memberRepository.findByMemberId(request.memberId());
        Long companyId = member.getCompany().getId();
        Company company = companyRepository.findByCompanyId(companyId);

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = s3Provider.upload(image);
        }

        Receipt receipt = Receipt.create(
                member.getName(),
                request.paymentDate(),
                request.approvalNumber(),
                request.storeAddress(),
                request.totalAmount(),
                imageUrl,
                company,
                request.memberId()
        );
        receiptRepository.save(receipt);

        return ReceiptSaveResponse.builder()
                .receiptId(receipt.getId())
                .build();
    }

    @Transactional
    public ImageUploadResponse upload(
            Long memberId,
            Long receiptId,
            MultipartFile image
    ) {
        memberRepository.findByMemberId(memberId);
        Receipt receipt = receiptRepository.findByReceiptId(receiptId);

        if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
            throw new EmptyFileExceptionCode();
        }

        String oldImageUrl = receipt.getUrl();
        if (oldImageUrl != null && !oldImageUrl.isBlank()) {
            s3Provider.delete(oldImageUrl);
        }

        String newImageUrl = s3Provider.upload(image);
        receipt.updateUrl(newImageUrl);

        return ImageUploadResponse.builder()
                .imageUrl(newImageUrl)
                .build();
    }

    @Transactional
    public List<Workspace> saveMcp(Long memberId, MultipartFile file) {
        Member member = memberRepository.findByMemberId(memberId);
        Company company = member.getValidatedCompany();

        List<Workspace> workspaces = company.getWorkspace();

        String imageUrl = s3Provider.upload(file);
        ReceiptSaveRequest request = ocr(file);

        ReceiptMcpRequest mcpRequest = ReceiptMcpRequest.builder()
                .approvalNumber(request.approvalNumber())
                .storeAddress(request.storeAddress())
                .totalAmount(request.totalAmount())
                .imageUrl(imageUrl)
                .paymentDate(request.paymentDate())
                .build();

        for (Workspace workspace : workspaces) {
            List<McpService> mcpServices = registry.getServices(workspace);
            for (McpService mcpService : mcpServices) {
                mcpService.createReceipt(mcpRequest, company.getId(), member);
            }
        }

        return company.getWorkspace();
    }

    @Transactional
    public ReceiptUpdateResponse updateReceipt(ReceiptUpdateServiceRequest request) {
        Member member = memberRepository.findByMemberId(request.memberId());
        Receipt receipt = receiptRepository.findByReceiptId(request.receiptId());

        receipt.validateUpdateReceipt(member.getId());

        receipt.updateReceipt(
                request.paymentDate(),
                request.approvalNumber(),
                request.storeAddress(),
                request.totalAmount()
        );

        return ReceiptUpdateResponse.builder()
                .ReceiptId(receipt.getId())
                .build();
    }

    public void deleteImageFromS3(Long memberId, String imageAddress) {
        Receipt receipt = receiptRepository.findByUrl(imageAddress);

        validateForbidden(memberId, receipt.getMemberId());

        s3Provider.delete(imageAddress);
    }

    @Transactional
    public void deleteReceipt(Long memberId, Long receiptId) {
        Receipt receipt = receiptRepository.findByReceiptId(receiptId);

        validateForbidden(memberId, receipt.getMemberId());

        if (receipt.getUrl() != null) {
            s3Provider.delete(receipt.getUrl());
        }
        receiptRepository.delete(receipt);
    }

    private ReceiptSaveRequest ocr(MultipartFile image) {
        String imageUrl = s3Provider.upload(image);

        String key = s3Provider.extractKeyFromUrl(imageUrl);

        byte[] imageBytes = s3Provider.getObjectBytes(key);
        ByteArrayResource imageResource = new ByteArrayResource(imageBytes);

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

    private void validateForbidden(Long memberId, Long receiptMemberId) {
        if (!receiptMemberId.equals(memberId)) {
            throw new DeleteNotForbiddenExceptionCode();
        }
    }

}
