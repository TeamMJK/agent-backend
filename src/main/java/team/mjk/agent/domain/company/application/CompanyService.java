package team.mjk.agent.domain.company.application;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import team.mjk.agent.domain.company.presentation.exception.InvalidInvitationCode;
import team.mjk.agent.domain.email.infrastructure.EmailSender;
import team.mjk.agent.domain.invitation.InvitationCodeProvider;
import team.mjk.agent.domain.invitation.domain.Invitation;
import team.mjk.agent.domain.invitation.domain.InvitationRepository;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.member.dto.response.MemberInfoGetResponse;
import team.mjk.agent.domain.member.presentation.exception.MemberNotFoundException;
import team.mjk.agent.domain.receipt.application.ReceiptService;
import team.mjk.agent.domain.receipt.domain.ReceiptRepository;
import team.mjk.agent.global.util.EmailMessageBuilder;
import team.mjk.agent.global.util.KmsUtil;

@RequiredArgsConstructor
@Service
public class CompanyService {

  private final InvitationCodeProvider invitationCodeProvider;
  private final InvitationRepository invitationRepository;
  private final CompanyRepository companyRepository;
  private final MemberRepository memberRepository;
  private final ReceiptService receiptService;
  private final EmailSender emailSender;
  private final EmailMessageBuilder emailMessageBuilder;
  private final KmsUtil kmsUtil;

  @Value("${spring.mail.username}")
  private String senderEmail;

  public CompanyInvitationEmailResponse createInvitationCodeAndSendEmail(Long memberId, CompanyInvitationEmailRequest request) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getCompany();

    Invitation invitation = invitationCodeProvider.create(company.getId());

    String subject = emailMessageBuilder.buildInvitationSubject();
    String content = emailMessageBuilder.buildInvitationMessage(invitation.getCode());

    emailSender.send(senderEmail, request.email(), subject, content);

    return CompanyInvitationEmailResponse.builder()
            .email(request.email())
            .invitationCode(invitation.getCode())
            .build();
  }

  @Transactional
  public Long create(CompanySaveRequest request, Long memberId) {
    Member member = memberRepository.findByMemberId(memberId);

    Company company = Company.create(request.name(), request.workspaces());
    companyRepository.save(company);

    member.saveCompany(company);
    return company.getId();
  }

  @Transactional
  public CompanyJoinResponse join(Long memberId, CompanyInvitationCodeRequest request) {
    Member member = memberRepository.findByMemberId(memberId);

    Invitation invitation = invitationRepository.findByCode(request.invitationCode())
        .orElseThrow(InvalidInvitationCode::new);

    Long companyId = invitation.getCompanyId();

    Company company = companyRepository.findById(companyId);

    member.saveCompany(company);

    invitationRepository.delete(invitation);

    return CompanyJoinResponse.builder()
            .companyName(company.getName())
            .build();
  }

  @Transactional(readOnly = true)
  public CompanyInfoResponse getCompanyInfo(Long memberId) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getCompany();


    return CompanyInfoResponse.builder()
        .workspaces(company.getWorkspace())
        .name(company.getName())
        .build();
  }

  @Transactional
  public Long update(Long memberId, CompanyUpdateRequest request) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getCompany();

    company.update(request.name(), request.workspaces());
    return company.getId();
  }

  @Transactional
  public Long delete(Long memberId) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getCompany();

    receiptService.getReceiptsByCompany(company)
            .forEach(receipt -> receiptService.deleteReceipt(memberId, receipt.getId()));
    memberRepository.deleteAllByCompanyId(company.getId());
    companyRepository.delete(company);

    return company.getId();
  }

  @Transactional(readOnly = true)
  public CompanyMemberListResponse getMembersInfo(Long memberId) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getCompany();

    List<Member> members = memberRepository.findAllByCompanyId(company.getId());
    List<MemberInfoGetResponse> memberInfoGetResponses = members.stream()
        .map(m -> Member.toMemberInfoGetResponse(m,kmsUtil))
        .collect(Collectors.toList());

    return CompanyMemberListResponse.builder()
        .members(memberInfoGetResponses)
        .build();
  }

}