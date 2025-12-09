package team.mjk.agent.domain.notion.application;


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
import team.mjk.agent.domain.companyworkspace.application.command.CompanyWorkspaceCommandService;
import team.mjk.agent.domain.companyworkspace.application.query.CompanyWorkspaceQueryService;
import team.mjk.agent.domain.companyworkspace.domain.Workspace;
import team.mjk.agent.domain.notion.domain.Notion;
import team.mjk.agent.domain.notion.domain.NotionRepository;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.notion.dto.request.NotionTokenUpdateRequest;
import team.mjk.agent.domain.notion.presentation.exception.NotionAPIException;
import team.mjk.agent.domain.receipt.presentation.request.ReceiptMcpRequest;
import team.mjk.agent.domain.mcp.McpService;
import team.mjk.agent.global.util.KmsUtil;

@RequiredArgsConstructor
@Service
public class NotionService implements McpService {

  private final NotionRepository notionRepository;
  private final MemberRepository memberRepository;
  private final CompanyWorkspaceQueryService companyWorkspaceQueryService;
  private final CompanyWorkspaceCommandService companyWorkspaceCommandService;
  private final KmsUtil kmsUtil;
  private final WebClient notionWebClient;



  @Transactional
  public Long update(Long memberId, NotionTokenUpdateRequest request) {
      Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getValidatedCompany();

    Notion notion = notionRepository.findByCompanyId(company.getId());
    notion.update(
            request.token(),
            request.businessTripDatabaseId(),
            request.receiptDatabaseId(),
            kmsUtil
    );
    return notion.getId();
  }

  @Transactional
  public Long delete(Long memberId) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getValidatedCompany();

    Notion notion = notionRepository.findByCompanyId(company.getId());
    notionRepository.delete(notion);

    List<Workspace> workspaces = companyWorkspaceQueryService.getWorkspacesByCompanyId(company.getId());
    workspaces.remove(Workspace.NOTION);

    if(workspaces.isEmpty()) {
      workspaces.add(Workspace.NONE);
    }

    companyWorkspaceCommandService.updateWorkspaces(company, workspaces);
    return notion.getId();
  }

    @Override
    public void createBusinessTrip(BusinessTripSaveRequest request, Long companyId, String requester) {
        Notion notion = notionRepository.findByCompanyId(companyId);

        Map<String, Object> payload =
                payloadFactory.businessTrip(request, notion, requester);

        notionProvider.send(payload, notion, kmsUtil.decrypt(notion.getToken()));
    }

    @Override
    public void createBusinessTripAgent(BusinessTripAgentRequest request, Long companyId, String requester) {
        Notion notion = notionRepository.findByCompanyId(companyId);

        Map<String, Object> payload =
                payloadFactory.businessTripAgent(request, notion, requester);

        notionProvider.send(payload, notion, kmsUtil.decrypt(notion.getToken()));
    }

    @Override
    public void createReceipt(ReceiptMcpRequest request, Long companyId, Member member) {
        Notion notion = notionRepository.findByCompanyId(companyId);

        Map<String, Object> payload =
                payloadFactory.receipt(request, notion, member);

        notionProvider.send(payload, notion, kmsUtil.decrypt(notion.getToken()));
    }


    @Override
  public Workspace getWorkspace() {
    return Workspace.NOTION;
  }

}