package team.mjk.agent.domain.email.presentation.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import team.mjk.agent.domain.email.application.dto.request.VerifyEmailServiceRequest;

@Builder
public record VerifyEmailRequest(

        @Email(message = "유효한 이메일 형식이어야 합니다.")
        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        String email,

        @NotBlank(message = "인증 코드는 필수 입력 값입니다.")
        String code

) {

    public VerifyEmailServiceRequest toServiceRequest() {
        return VerifyEmailServiceRequest.builder()
                .email(email)
                .code(code)
                .build();
    }

}
