package team.mjk.agent.domain.company.dto.response;

import lombok.Builder;

@Builder
public record CompanyInvitationEmailResponse(

        String email,

        String invitationCode

) {
}
