package team.mjk.agent.domain.company.application.dto.request;

import lombok.Builder;

@Builder
public record CompanyInvitationEmailServiceRequest(

        Long memberId,

        String email

) {
}
