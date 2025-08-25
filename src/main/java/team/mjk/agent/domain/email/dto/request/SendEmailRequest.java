package team.mjk.agent.domain.email.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SendEmailRequest(

        @Email(message = "유효한 이메일 형식이어야 합니다.")
        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        String email

) {
}
