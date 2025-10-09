package team.mjk.agent.domain.passport.application.dto.response;

import lombok.Builder;

@Builder
public record PassportSaveResponse(

        Long passPortId

) {
}
