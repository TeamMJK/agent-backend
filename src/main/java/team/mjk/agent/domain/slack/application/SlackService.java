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
import team.mjk.agent.domain.company.domain.Workspace;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.receipt.dto.request.ReceiptMcpRequest;
import team.mjk.agent.domain.slack.domain.Slack;
import team.mjk.agent.domain.slack.domain.SlackRepository;
import team.mjk.agent.domain.slack.dto.request.SlackSaveRequest;
import team.mjk.agent.domain.slack.dto.request.SlackUpdateRequest;
import team.mjk.agent.global.mcp.McpService;
import team.mjk.agent.global.util.KmsUtil;

@Service
@RequiredArgsConstructor
public class SlackService implements McpService {

  private final SlackRepository slackRepository;
  private final MemberRepository memberRepository;
  private final KmsUtil kmsUtil;
  private final WebClient slackWebClient;

  public Long save(Long memberId, SlackSaveRequest request) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getCompany();

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
    Company company = member.getCompany();

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
    Company company = member.getCompany();

    Slack slack = slackRepository.findByCompanyId(company.getId());
    slackRepository.delete(slack);

    company.updateWorkspace(Workspace.NONE);
    return slack.getId();
  }

  @Override
  public void createBusinessTrip(BusinessTripSaveRequest request, Long companyId) {
    Slack slack = slackRepository.findByCompanyId(companyId);

    String token = kmsUtil.decrypt(slack.getToken());
    String channelId = kmsUtil.decrypt(slack.getChannelId());

    String uri = "/chat.postMessage";

    List<String> members = request.names();
    String memberStr = String.join(", ", members);
    String message = String.format(
        "출장 예약: %s ~ %s\n장소: %s\n숙박/항공: %s\n출장 인원: %s",
        request.departDate(),
        request.arriveDate(),
        request.destination(),
        request.serviceType().getCategory(),
        memberStr
    );

    slackWebClient.post()
        .uri(uri)
        .header("Authorization","Bearer "+token)
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
  public void createBusinessTripAgent(BusinessTripAgentRequest request, Long companyId) {
    Slack slack = slackRepository.findByCompanyId(companyId);

    String token = kmsUtil.decrypt(slack.getToken());
    String channelId = kmsUtil.decrypt(slack.getChannelId());

    String uri = "/chat.postMessage";

    List<String> members = request.names();
    String memberStr = String.join(", ", members);
    String message = String.format(
        "출장 예약: %s ~ %s\n장소: %s\n숙박/항공: %s\n출장 인원: %s",
        request.departDate(),
        request.arriveDate(),
        request.destination(),
        request.serviceType(),
        memberStr
    );

    slackWebClient.post()
        .uri(uri)
        .header("Authorization","Bearer "+token)
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
  public void createReceipt(ReceiptMcpRequest request, Long companyId) {
    Slack slack = slackRepository.findByCompanyId(companyId);

    String token = kmsUtil.decrypt(slack.getToken());
    String channelId = kmsUtil.decrypt(slack.getChannelId());
    String uri = "/chat.postMessage";

    String message = String.format(
        "날짜: %s\n주문번호: %s\n주소: %s\n총금액: %s\n이름: %s\n이미지 주소: %s",
        request.paymentDate(),
        request.approvalNumber(),
        request.storeAddress(),
        request.totalAmount(),
        request.name(),
        request.imageUrl()
    );

    slackWebClient.post()
        .uri(uri)
        .header("Authorization","Bearer "+token)
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
  public Workspace getWorkspace() {
    return Workspace.SLACK;
  }

}