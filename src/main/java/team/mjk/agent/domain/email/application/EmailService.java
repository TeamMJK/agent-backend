package team.mjk.agent.domain.email.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import team.mjk.agent.domain.email.application.dto.request.SendEmailServiceRequest;
import team.mjk.agent.domain.email.application.dto.request.VerifyEmailServiceRequest;
import team.mjk.agent.domain.email.application.dto.response.SendEmailResponse;
import team.mjk.agent.domain.email.application.dto.response.VerifyEmailResponse;
import team.mjk.agent.domain.email.infrastructure.EmailSender;
import team.mjk.agent.domain.email.presentation.exception.InvalidCodeException;
import team.mjk.agent.global.util.AuthCodeGenerator;
import team.mjk.agent.global.util.EmailMessageBuilder;

@RequiredArgsConstructor
@Service
public class EmailService {

    private final EmailSender emailSender;
    private final AuthCodeGenerator codeGenerator;
    private final AuthCodeService authCodeService;
    private final EmailMessageBuilder messageBuilder;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public SendEmailResponse sendAuthEmail(SendEmailServiceRequest request) {
        String code = codeGenerator.generateCode(6);
        String subject = messageBuilder.buildAuthSubject();
        String content = messageBuilder.buildAuthMessage(code);

        emailSender.send(senderEmail, request.email(), subject, content);

        authCodeService.saveAuthCode(code, request.email());

        return SendEmailResponse.builder()
                .code(code)
                .build();
    }

    public VerifyEmailResponse verifyAuthCode(VerifyEmailServiceRequest request) {
        boolean isValid = authCodeService.validateAuthCode(request.email(), request.code());

        if (!isValid) {
            throw new InvalidCodeException();
        }

        authCodeService.deleteAuthCode(request.code());

        return VerifyEmailResponse.builder()
                .success(true)
                .build();
    }

}
