package team.mjk.agent.domain.passport.application.dto.request;

import lombok.Builder;

@Builder
public record PassportUpdateServiceRequest(

        Long memberId,

        String passportNumber,

        String passportExpireDate

) {
}
