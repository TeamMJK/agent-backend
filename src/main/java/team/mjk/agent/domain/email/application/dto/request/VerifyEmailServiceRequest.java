package team.mjk.agent.domain.email.application.dto.request;

import lombok.Builder;

@Builder
public record VerifyEmailServiceRequest(

        String email,

        String code

) {
}
