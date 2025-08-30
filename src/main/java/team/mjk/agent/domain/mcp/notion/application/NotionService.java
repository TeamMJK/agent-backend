package team.mjk.agent.domain.mcp.notion.application;


import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripAgentRequest;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripSaveRequest;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.company.domain.Workspace;
import team.mjk.agent.domain.mcp.notion.domain.Notion;
import team.mjk.agent.domain.mcp.notion.domain.NotionRepository;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.mcp.notion.dto.request.NotionTokenRequest;
import team.mjk.agent.domain.mcp.notion.dto.request.NotionTokenUpdateRequest;
import team.mjk.agent.domain.mcp.notion.presentation.exception.NotionAPIException;
import team.mjk.agent.domain.receipt.dto.request.ReceiptMcpRequest;
import team.mjk.agent.domain.mcp.McpService;
import team.mjk.agent.global.util.KmsUtil;

@RequiredArgsConstructor
@Service
public class NotionService implements McpService {

  private final NotionRepository notionRepository;
  private final MemberRepository memberRepository;
  private final KmsUtil kmsUtil;
  private final WebClient notionWebClient;

  public Long save(Long memberId, NotionTokenRequest request) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getCompany();

    String encryptToken = kmsUtil.encrypt(request.token());
    String encryptBusinessTripDatabaseId = kmsUtil.encrypt(request.businessTripDatabaseId());
    String encryptReceiptDatabaseId = kmsUtil.encrypt(request.receiptDatabaseId());

    Notion notion = Notion.create(
        encryptToken,
        encryptBusinessTripDatabaseId,
        company.getId(),
        encryptReceiptDatabaseId
    );

    notionRepository.save(notion);
    return notion.getId();
  }

  @Transactional
  public Long update(Long memberId, NotionTokenUpdateRequest request) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getCompany();

    Notion notion = notionRepository.findByCompanyId(company.getId());
    notion.update(request.token(), request.businessTripDatabaseId(),
        kmsUtil,
        request.receiptDatabaseId());
    return notion.getId();
  }

  @Transactional
  public Long delete(Long memberId) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getCompany();

    Notion notion = notionRepository.findByCompanyId(company.getId());
    notionRepository.delete(notion);

    List<Workspace> workspaces = company.getWorkspace();
    workspaces.remove(Workspace.NOTION);

    if(workspaces.isEmpty()) {
      workspaces.add(Workspace.NONE);
    }

    company.updateWorkspace(workspaces);
    return notion.getId();
  }

  private void sendNotionMessage(Map<String, Object> payload, Notion notion) {
    notionWebClient.post()
        .uri("/pages")
        .header("Authorization", "Bearer " + kmsUtil.decrypt(notion.getToken()))
        .header("Content-Type", "application/json")
        .bodyValue(payload)
        .retrieve()
        .onStatus(status -> !status.is2xxSuccessful(),
            response -> Mono.error(new NotionAPIException()))
        .bodyToMono(String.class)
        .block();
  }

  @Override
  public void createBusinessTrip(BusinessTripSaveRequest request, Long companyId, String requester) {
    Notion notion = notionRepository.findByCompanyId(companyId);

    List<Map<String, Map<String, String>>> nameBlocks = request.names().stream()
        .map(name -> Map.of("text", Map.of("content", name)))
        .toList();

    Map<String, Object> payload = Map.of(
        "parent", Map.of("database_id", kmsUtil.decrypt(notion.getBusinessTripDatabaseId())),
        "properties", Map.of(
            "이름", Map.of("title", nameBlocks),
            "도착일자", Map.of("rich_text", List.of(
                Map.of("text", Map.of("content", request.arriveDate().toString()))
            )),
            "출발일자", Map.of("rich_text", List.of(
                Map.of("text", Map.of("content", request.departDate().toString()))
            )),
            "출장지", Map.of("rich_text", List.of(
                Map.of("text", Map.of("content", request.destination()))
            )),
            "카테고리", Map.of("rich_text", List.of(
                Map.of("text", Map.of("content", request.serviceType().getCategory()))
            ))
        )
    );

    sendNotionMessage(payload, notion);
  }

  @Override
  public void createBusinessTripAgent(BusinessTripAgentRequest request, Long companyId, String requester) {
    Notion notion = notionRepository.findByCompanyId(companyId);

    List<Map<String, Map<String, String>>> nameBlocks = request.names().stream()
        .map(name -> Map.of("text", Map.of("content", name)))
        .toList();

    Map<String, Object> payload = Map.of(
        "parent", Map.of("database_id", kmsUtil.decrypt(notion.getBusinessTripDatabaseId())),
        "properties", Map.of(
            "이름", Map.of("title", nameBlocks),
            "도착일자", Map.of("rich_text", List.of(
                Map.of("text", Map.of("content", request.arriveDate()))
            )),
            "출발일자", Map.of("rich_text", List.of(
                Map.of("text", Map.of("content", request.departDate()))
            )),
            "출장지", Map.of("rich_text", List.of(
                Map.of("text", Map.of("content", request.destination()))
            )),
            "카테고리", Map.of("rich_text", List.of(
                Map.of("text", Map.of("content", request.serviceType()))
            ))
        )
    );

    sendNotionMessage(payload, notion);
  }

  @Override
  public void createReceipt(ReceiptMcpRequest request ,Long companyId,  Member member) {
    Notion notion = notionRepository.findByCompanyId(companyId);

    Map<String, Object> payload = Map.of(
        "parent", Map.of("database_id", kmsUtil.decrypt(notion.getReceiptDatabaseId())),
        "properties", Map.of(
            "이름", Map.of("rich_text", List.of(
                Map.of("text", Map.of("content", request.name()))
            )),
            "승인번호", Map.of("rich_text", List.of(
                Map.of("text", Map.of("content", request.approvalNumber()))
            )),
            "주소", Map.of("rich_text", List.of(
                Map.of("text", Map.of("content", request.storeAddress()))
            )),
            "총금액", Map.of("rich_text", List.of(
                Map.of("text", Map.of("content", request.totalAmount().toString()))
            )),
            "이미지", Map.of("url", request.imageUrl()),
            "거래일자", Map.of("rich_text", List.of(
                Map.of("text", Map.of("content", request.paymentDate().toString()))
            ))
        )
    );

    sendNotionMessage(payload, notion);
  }

  @Override
  public Workspace getWorkspace() {
    return Workspace.NOTION;
  }

}