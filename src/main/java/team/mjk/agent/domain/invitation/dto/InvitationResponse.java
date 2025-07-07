package team.mjk.agent.domain.invitation.dto;

import lombok.Builder;

@Builder
public record InvitationResponse(
        Long companyId,
        String code,
        long expirationInMinutes
) {

}