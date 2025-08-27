package team.mjk.agent.domain.company.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.company.domain.CompanyRepository;
import team.mjk.agent.domain.company.dto.request.CompanyInvitationCodeRequest;
import team.mjk.agent.domain.company.dto.request.CompanyInvitationEmailRequest;
import team.mjk.agent.domain.company.dto.request.CompanySaveRequest;
import team.mjk.agent.domain.company.dto.response.CompanyInvitationEmailResponse;
import team.mjk.agent.domain.company.dto.response.CompanyJoinResponse;
import team.mjk.agent.domain.company.presentation.exception.CompanyNotFoundException;
import team.mjk.agent.domain.company.presentation.exception.InvalidInvitationCode;
import team.mjk.agent.domain.email.infrastructure.EmailSender;
import team.mjk.agent.domain.invitation.InvitationCodeProvider;
import team.mjk.agent.domain.invitation.domain.Invitation;
import team.mjk.agent.domain.invitation.domain.InvitationRepository;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.member.presentation.exception.MemberNotFoundException;
import team.mjk.agent.global.util.EmailMessageBuilder;

@RequiredArgsConstructor
@Service
public class CompanyService {

  private final InvitationCodeProvider invitationCodeProvider;
  private final InvitationRepository invitationRepository;
  private final CompanyRepository companyRepository;
  private final MemberRepository memberRepository;
  private final EmailSender emailSender;
  private final EmailMessageBuilder emailMessageBuilder;

  @Value("${spring.mail.username}")
  private String senderEmail;

  public CompanyInvitationEmailResponse createInvitationCodeAndSendEmail(Long memberId, CompanyInvitationEmailRequest request) {
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(MemberNotFoundException::new);

    if (member.getCompany() == null) {
      throw new CompanyNotFoundException();
    }

    Company company = companyRepository.findById(member.getCompany().getId());

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
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(MemberNotFoundException::new);

    Company company = Company.create(request.name(), request.workspace());
    companyRepository.save(company);
    member.saveCompany(company);
    return company.getId();
  }

  @Transactional
  public CompanyJoinResponse join(Long memberId, CompanyInvitationCodeRequest request) {
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(MemberNotFoundException::new);

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
  public String getCompanyName(Long memberId) {
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(MemberNotFoundException::new);

    return member.getCompany().getName();
  }

}