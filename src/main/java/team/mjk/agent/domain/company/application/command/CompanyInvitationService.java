package team.mjk.agent.domain.company.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.company.application.dto.response.CompanyInvitationEmailResponse;
import team.mjk.agent.domain.company.presentation.exception.InvalidInvitationCode;
import team.mjk.agent.domain.email.infrastructure.EmailSender;
import team.mjk.agent.domain.invitation.InvitationCodeProvider;
import team.mjk.agent.domain.invitation.domain.Invitation;
import team.mjk.agent.domain.invitation.domain.InvitationRepository;
import team.mjk.agent.global.util.EmailMessageBuilder;

@RequiredArgsConstructor
@Service
public class CompanyInvitationService {

    private final InvitationCodeProvider invitationCodeProvider;
    private final InvitationRepository invitationRepository;
    private final EmailSender emailSender;
    private final EmailMessageBuilder emailMessageBuilder;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Transactional
    public CompanyInvitationEmailResponse createInvitation(Long companyId, String email) {
        Invitation invitation = invitationCodeProvider.create(companyId);

        String subject = emailMessageBuilder.buildInvitationSubject();
        String content = emailMessageBuilder.buildInvitationMessage(invitation.getCode());

        emailSender.send(senderEmail, email, subject, content);

        return CompanyInvitationEmailResponse.builder()
                .email(email)
                .invitationCode(invitation.getCode())
                .build();
    }

    @Transactional(readOnly = true)
    public Invitation getByCode(String code) {
        return invitationRepository.findByCode(code)
                .orElseThrow(InvalidInvitationCode::new);
    }

    @Transactional
    public void delete(Invitation invitation) {
        invitationRepository.delete(invitation);
    }

}
