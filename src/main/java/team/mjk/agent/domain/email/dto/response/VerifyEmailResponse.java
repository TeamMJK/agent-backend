package team.mjk.agent.domain.email.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "이메일 인증 검증 응답 DTO")
public record VerifyEmailResponse(

        @Schema(description = "인증 성공 여부", example = "true")
        boolean success

) {}
