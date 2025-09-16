package team.mjk.agent.domain.email.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.mjk.agent.domain.email.application.EmailService;
import team.mjk.agent.domain.email.dto.request.SendEmailRequest;
import team.mjk.agent.domain.email.dto.request.VerifyEmailRequest;
import team.mjk.agent.domain.email.dto.response.SendEmailResponse;
import team.mjk.agent.domain.email.dto.response.VerifyEmailResponse;

@RequiredArgsConstructor
@RequestMapping("/emails")
@RestController
public class EmailController implements EmailDocsController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<SendEmailResponse> sendAuthEmail(
            @Valid @RequestBody SendEmailRequest request
    ) {
        SendEmailResponse response = emailService.sendAuthEmail(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<VerifyEmailResponse> verifyAuthCode(
            @Valid @RequestBody VerifyEmailRequest request
    ) {
        VerifyEmailResponse response = emailService.verifyAuthCode(request.email(), request.code());
        return ResponseEntity.ok(response);
    }

}
