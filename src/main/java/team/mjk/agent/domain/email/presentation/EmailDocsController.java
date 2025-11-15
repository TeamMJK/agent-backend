package team.mjk.agent.domain.email.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import team.mjk.agent.domain.email.application.dto.response.SendEmailResponse;
import team.mjk.agent.domain.email.application.dto.response.VerifyEmailResponse;
import team.mjk.agent.domain.email.presentation.request.SendEmailRequest;
import team.mjk.agent.domain.email.presentation.request.VerifyEmailRequest;

@Tag(name = "Email", description = "이메일 인증 관련 API")
@RequestMapping("/emails")
public interface EmailDocsController {

    @Operation(summary = "인증 이메일 전송", description = "사용자 이메일로 인증 코드를 전송합니다.")
    @PostMapping("/send")
    ResponseEntity<SendEmailResponse> sendAuthEmail(@Valid @RequestBody SendEmailRequest request);

    @Operation(summary = "이메일 인증 코드 검증", description = "사용자가 입력한 인증 코드를 검증합니다.")
    @PostMapping("/verify")
    ResponseEntity<VerifyEmailResponse> verifyAuthCode(@Valid @RequestBody VerifyEmailRequest request);

}
