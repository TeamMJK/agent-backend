package team.mjk.agent.domain.email.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.mjk.agent.domain.email.application.EmailService;
import team.mjk.agent.domain.email.application.dto.response.SendEmailResponse;
import team.mjk.agent.domain.email.application.dto.response.VerifyEmailResponse;
import team.mjk.agent.domain.email.presentation.request.SendEmailRequest;
import team.mjk.agent.domain.email.presentation.request.VerifyEmailRequest;

@RequiredArgsConstructor
@RequestMapping("/emails")
@RestController
public class EmailController implements EmailDocsController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<SendEmailResponse> sendAuthEmail(
            @Valid @RequestBody SendEmailRequest request
    ) {
        SendEmailResponse response = emailService.sendAuthEmail(request.toServiceRequest());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<VerifyEmailResponse> verifyAuthCode(
            @Valid @RequestBody VerifyEmailRequest request
    ) {
        VerifyEmailResponse response = emailService.verifyAuthCode(request.toServiceRequest());
        return ResponseEntity.ok(response);
    }

}
