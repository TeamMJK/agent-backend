package team.mjk.agent.domain.slack.application;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripAgentRequest;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripSaveRequest;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.companyworkspace.application.command.CompanyWorkspaceCommandService;
import team.mjk.agent.domain.companyworkspace.application.query.CompanyWorkspaceQueryService;
import team.mjk.agent.domain.companyworkspace.domain.Workspace;
import team.mjk.agent.domain.slack.domain.Slack;
import team.mjk.agent.domain.slack.domain.SlackRepository;
import team.mjk.agent.domain.slack.dto.request.SlackSaveRequest;
import team.mjk.agent.domain.slack.dto.request.SlackUpdateRequest;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.receipt.presentation.request.ReceiptMcpRequest;
import team.mjk.agent.domain.mcp.McpService;
import team.mjk.agent.global.util.KmsUtil;

@Service
@RequiredArgsConstructor
public class SlackService implements McpService {

  private final SlackRepository slackRepository;
  private final MemberRepository memberRepository;
  private final KmsUtil kmsUtil;
  private final WebClient slackWebClient;
  private final CompanyWorkspaceQueryService companyWorkspaceQueryService;
  private final CompanyWorkspaceCommandService companyWorkspaceCommandService;

  public Long save(Long memberId, SlackSaveRequest request) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getValidatedCompany();

    String encryptToken = kmsUtil.encrypt(request.token());
    String channelId = kmsUtil.encrypt(request.channelId());

    Slack slack = Slack.create(
        encryptToken,
        channelId,
        company.getId()
    );

    slackRepository.save(slack);
    return slack.getId();
  }

  @Transactional
  public Long update(Long memberId, SlackUpdateRequest request) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getValidatedCompany();

    Slack slack = slackRepository.findByCompanyId(company.getId());
    slack.update(
        request.token(),
        request.channelId(),
        kmsUtil
    );

    return slack.getId();
  }

  @Transactional
  public Long delete(Long memberId) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getValidatedCompany();

    Slack slack = slackRepository.findByCompanyId(company.getId());
    slackRepository.delete(slack);

    List<Workspace> workspaces = companyWorkspaceQueryService.getWorkspacesByCompanyId(company.getId());
    workspaces.remove(Workspace.SLACK);

    if(workspaces.isEmpty()) {
      workspaces.add(Workspace.NONE);
    }

    companyWorkspaceCommandService.updateWorkspaces(company, workspaces);
    return slack.getId();
  }

  private void sendSlackMessage(String token, String channelId, String message) {
    String uri = "/chat.postMessage";

    slackWebClient.post()
        .uri(uri)
        .header("Authorization", "Bearer " + token)
        .header("Content-Type", "application/json; charset=utf-8")
        .bodyValue(Map.of(
            "channel", channelId,
            "text", message
        ))
        .retrieve()
        .bodyToMono(String.class)
        .doOnError(e -> System.err.println("Slack API 호출 실패: " + e.getMessage()))
        .subscribe(response -> System.out.println("Slack API 응답: " + response));
  }

  @Override
  public void createBusinessTrip(BusinessTripSaveRequest request, Long companyId, String requester) {
    Slack slack = slackRepository.findByCompanyId(companyId);

    String token = kmsUtil.decrypt(slack.getToken());
    String channelId = kmsUtil.decrypt(slack.getChannelId());

    List<String> members = request.names();
    String memberStr = String.join(", ", members);
    String message = String.format(
        "작성자: %s\n출장 예약: %s ~ %s\n장소: %s\n숙박/항공: %s\n출장 인원: %s",
        requester,
        request.departDate(),
        request.arriveDate(),
        request.destination(),
        request.serviceType().getCategory(),
        memberStr
    );

    sendSlackMessage(token, channelId, message);
  }

  @Override
  public void createBusinessTripAgent(BusinessTripAgentRequest request, Long companyId, String requester) {
    Slack slack = slackRepository.findByCompanyId(companyId);

    String token = kmsUtil.decrypt(slack.getToken());
    String channelId = kmsUtil.decrypt(slack.getChannelId());

    List<String> members = request.names();
    String memberStr = String.join(", ", members);
    String message = String.format(
        "작성자: %s\n출장 예약: %s ~ %s\n장소: %s\n숙박/항공: %s\n출장 인원: %s",
        requester,
        request.departDate(),
        request.arriveDate(),
        request.destination(),
        request.serviceType(),
        memberStr
    );

    sendSlackMessage(token, channelId, message);
  }

  @Override
  public void createReceipt(ReceiptMcpRequest request, Long companyId,  Member member) {
    Slack slack = slackRepository.findByCompanyId(companyId);

    String token = kmsUtil.decrypt(slack.getToken());
    String channelId = kmsUtil.decrypt(slack.getChannelId());

    String message = String.format(
        "작성자: %s\n날짜: %s\n주문번호: %s\n주소: %s\n총금액: %s\n이미지 주소: %s",
        member.getName(),
        request.paymentDate(),
        request.approvalNumber(),
        request.storeAddress(),
        request.totalAmount(),
        request.imageUrl()
    );

    sendSlackMessage(token, channelId, message);
  }

  @Override
  public Workspace getWorkspace() {
    return Workspace.SLACK;
  }
}
