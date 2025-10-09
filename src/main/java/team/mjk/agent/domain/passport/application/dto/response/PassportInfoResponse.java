package team.mjk.agent.domain.passport.application.dto.response;

import lombok.Builder;
import team.mjk.agent.domain.passport.domain.Passport;
import team.mjk.agent.global.util.KmsUtil;

@Builder
public record PassportInfoResponse(

        Long memberId,

        Long passPortId,

        String passportNumber,

        String passportExpireDate

) {

    public static PassportInfoResponse from(Passport passport, KmsUtil kmsUtil) {
        return PassportInfoResponse.builder()
                .memberId(passport.getMember().getId())
                .passPortId(passport.getId())
                .passportNumber(kmsUtil.decrypt(passport.getPassportNumber()))
                .passportExpireDate(kmsUtil.decrypt(passport.getPassportExpireDate()))
                .build();
    }

}
