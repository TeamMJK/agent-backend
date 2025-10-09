package team.mjk.agent.domain.passport.application.dto.response;

import lombok.Builder;

@Builder
public record PassportUpdateResponse(

        Long passPortId

) {
}
