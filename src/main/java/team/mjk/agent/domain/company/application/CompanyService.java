package team.mjk.agent.domain.company.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.company.domain.CompanyRepository;
import team.mjk.agent.domain.company.dto.request.CompanyInvitationCodeRequest;
import team.mjk.agent.domain.company.dto.request.CompanyInvitationEmailRequest;
import team.mjk.agent.domain.company.dto.request.CompanySaveRequest;
import team.mjk.agent.domain.company.dto.request.CompanyUpdateRequest;
import team.mjk.agent.domain.company.dto.response.CompanyInfoResponse;
import team.mjk.agent.domain.company.dto.response.CompanyInvitationEmailResponse;
import team.mjk.agent.domain.company.dto.response.CompanyJoinResponse;
import team.mjk.agent.domain.company.dto.response.CompanyMemberListResponse;
import team.mjk.agent.domain.invitation.domain.Invitation;
import team.mjk.agent.domain.mcp.notion.domain.Notion;
import team.mjk.agent.domain.mcp.notion.domain.NotionRepository;
import team.mjk.agent.domain.mcp.slack.domain.Slack;
import team.mjk.agent.domain.mcp.slack.domain.SlackRepository;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.member.application.dto.response.MemberInfoGetResponse;
import team.mjk.agent.domain.passport.domain.Passport;
import team.mjk.agent.domain.passport.domain.PassportRepository;
import team.mjk.agent.global.util.KmsUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CompanyService {

  private final CompanyRepository companyRepository;
  private final NotionRepository notionRepository;
  private final MemberRepository memberRepository;
  private final SlackRepository slackRepository;
  private final KmsUtil kmsUtil;
  private final CompanyCleanupService companyCleanupService;
  private final CompanyInvitationService companyInvitationService;
  private final PassportRepository passportRepository;

  public CompanyInvitationEmailResponse createInvitationCodeAndSendEmail(
          Long memberId,
          CompanyInvitationEmailRequest request
  ) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getValidatedCompany();

    return companyInvitationService.createInvitation(company.getId(), request.email());
  }

  @Transactional
  public Long create(CompanySaveRequest request, Long memberId) {
    Member member = memberRepository.findByMemberId(memberId);

    Company company = Company.create(request.name(), request.workspaces());
    companyRepository.save(company);

    if (request.workspaceConfigs().getNotionTokenRequest() != null) {
      Notion notion = Notion.create(
          kmsUtil.encrypt(request.workspaceConfigs().getNotionTokenRequest().token()),
          kmsUtil.encrypt(request.workspaceConfigs().getNotionTokenRequest().businessTripDatabaseId()),
          company.getId(),
          kmsUtil.encrypt(request.workspaceConfigs().getNotionTokenRequest().receiptDatabaseId())
      );
      notionRepository.save(notion);
    }

    if (request.workspaceConfigs().getSlackSaveRequest() != null) {
      Slack slack = Slack.create(
          kmsUtil.encrypt(request.workspaceConfigs().getSlackSaveRequest().token()),
          kmsUtil.encrypt(request.workspaceConfigs().getSlackSaveRequest().channelId()),
          company.getId()
      );
      slackRepository.save(slack);
    }

    member.saveCompany(company);
    return company.getId();
  }

  @Transactional
  public CompanyJoinResponse join(Long memberId, CompanyInvitationCodeRequest request) {
    Member member = memberRepository.findByMemberId(memberId);

    Invitation invitation = companyInvitationService.getByCode(request.invitationCode());
    Company company = companyRepository.findById(invitation.getCompanyId());

    member.saveCompany(company);
    companyInvitationService.delete(invitation);

    return CompanyJoinResponse.builder()
            .companyName(company.getName())
            .build();
  }

  @Transactional(readOnly = true)
  public CompanyInfoResponse getCompanyInfo(Long memberId) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getValidatedCompany();

    return CompanyInfoResponse.builder()
        .workspaces(company.getWorkspace())
        .name(company.getName())
        .build();
  }

  @Transactional
  public Long update(Long memberId, CompanyUpdateRequest request) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getValidatedCompany();

    if (request.workspaceConfigs().getNotionTokenRequest() != null) {
      Notion notion = Notion.create(
          kmsUtil.encrypt(request.workspaceConfigs().getNotionTokenRequest().token()),
          kmsUtil.encrypt(request.workspaceConfigs().getNotionTokenRequest().businessTripDatabaseId()),
          company.getId(),
          kmsUtil.encrypt(request.workspaceConfigs().getNotionTokenRequest().receiptDatabaseId())
      );
      notionRepository.save(notion);
    } else {
      Optional<Notion> notion = notionRepository.findOptionalByCompanyId(company.getId());
      notion.ifPresent(notionRepository::delete);
    }

    if (request.workspaceConfigs().getSlackSaveRequest() != null) {
      Slack slack = Slack.create(
          kmsUtil.encrypt(request.workspaceConfigs().getSlackSaveRequest().token()),
          kmsUtil.encrypt(request.workspaceConfigs().getSlackSaveRequest().channelId()),
          company.getId()
      );
      slackRepository.save(slack);
    } else {
      Optional<Slack> slack = slackRepository.findOptionalByCompanyId(company.getId());
      slack.ifPresent(slackRepository::delete);
    }

    company.update(request.name(), request.workspaces());
    return company.getId();
  }

  @Transactional
  public Long delete(Long memberId) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getValidatedCompany();

    companyCleanupService.cleanupCompany(company, memberId);
    companyRepository.delete(company);

    return company.getId();
  }

  @Transactional(readOnly = true)
  public CompanyMemberListResponse getMembersInfo(Long memberId) {

    log.info("===== [1] Member 단건 조회 시작 =====");
    Member member = memberRepository.findByMemberId(memberId);
    log.info("===== [1] Member 조회 완료. (쿼리 1회 발생) =====\n");


    log.info("===== [2] Company Lazy 조회 시작 =====");
    Company company = member.getValidatedCompany();
    log.info("===== [2] Company Lazy 조회 완료. (쿼리 1회 발생) =====\n");


    log.info("===== [3] Company 소속 Member 리스트 조회 시작 =====");
    List<Member> members = memberRepository.findAllByCompanyId(company.getId());
    log.info("===== [3] Member 리스트 조회 완료. (쿼리 1회 발생) =====\n");


    log.info("===== [4] MemberInfo 매핑 시작 - 여기서 N+1 문제 발생 =====");
    List<MemberInfoGetResponse> memberInfoGetResponses = members.stream()
            .map(m -> {
              log.info("[Lazy Loading] Member(id: {}) 연관 엔티티 접근 -> 추가 쿼리 발생", m.getId());
              return MemberInfoGetResponse.from(m, kmsUtil);
            })
            .collect(Collectors.toList());
    log.info("===== [4] MemberInfo 매핑 완료. (총 {}개의 Lazy 추가 쿼리 발생) =====\n", members.size());


    log.info("===== [5] 최종 응답 생성 완료 =====");

    return CompanyMemberListResponse.builder()
            .members(memberInfoGetResponses)
            .build();
  }

}