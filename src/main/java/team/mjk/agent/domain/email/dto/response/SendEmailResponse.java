package team.mjk.agent.domain.email.dto.response;

import lombok.Builder;

@Builder
public record SendEmailResponse(

        String code

) {
}
