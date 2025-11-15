package team.mjk.agent.domain.email.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "이메일 발송 응답 DTO")
public record SendEmailResponse(

        @Schema(description = "인증 코드", example = "123456")
        String code

) {}
