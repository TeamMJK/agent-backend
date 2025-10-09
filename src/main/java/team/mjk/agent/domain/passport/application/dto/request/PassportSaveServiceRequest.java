package team.mjk.agent.domain.passport.application.dto.request;

import lombok.Builder;

@Builder
public record PassportSaveServiceRequest(

        Long memberId,

        String passportNumber,

        String passportExpireDate

) {
}
