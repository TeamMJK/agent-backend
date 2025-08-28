package team.mjk.agent.domain.slack.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripAgentRequest;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripSaveRequest;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.company.domain.Workspace;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.member.presentation.exception.MemberNotFoundException;
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

  public Long save(Long memberId, SlackSaveRequest request) {
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(MemberNotFoundException::new);
    Long companyId = member.getCompany().getId();

    String encryptToken = kmsUtil.encrypt(request.token());
    String channelId = kmsUtil.encrypt(request.channelId());

    Slack slack = Slack.create(
        encryptToken,
        channelId,
        companyId
    );

    slackRepository.save(slack);
    return slack.getId();
  }

  @Transactional
  public Long update(Long memberId, SlackUpdateRequest request) {
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(MemberNotFoundException::new);
    Long companyId = member.getCompany().getId();

    Slack slack = slackRepository.findByCompanyId(companyId);
    slack.update(
        request.token(),
        request.channelId(),
        kmsUtil
    );

    return slack.getId();
  }

  @Transactional
  public Long delete(Long memberId) {
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(MemberNotFoundException::new);
    Company company = member.getCompany();

    Slack slack = slackRepository.findByCompanyId(company.getId());
    slackRepository.delete(slack);

    company.updateWorkspace(Workspace.NONE);
    return slack.getId();
  }

  @Override
  public void createBusinessTrip(BusinessTripSaveRequest request, Long companyId) {

  }

  @Override
  public void createBusinessTripAgent(BusinessTripAgentRequest request, Long companyId) {

  }

  @Override
  public Workspace getWorkspace() {
    return Workspace.SLACK;
  }

  @Override
  public void createReceipt(ReceiptMcpRequest request, Long companyId) {

  }
}
