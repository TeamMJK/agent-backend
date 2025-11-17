package team.mjk.agent.domain.member.application.dto.response;

import lombok.Builder;
import team.mjk.agent.domain.member.domain.Gender;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.global.util.KmsUtil;

@Builder
public record MemberGetInfoResponse2(
        String name,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        Gender gender,
        String birthDate,
        String passportNumber,
        String passportExpireDate
) {

    public static MemberGetInfoResponse2 from(MemberGetInfoResponse2 rawDto, KmsUtil kmsUtil) {
        return new MemberGetInfoResponse2(
                rawDto.name(),
                rawDto.email(),
                kmsUtil.decrypt(rawDto.firstName()),
                kmsUtil.decrypt(rawDto.lastName()),
                kmsUtil.decrypt(rawDto.phoneNumber()),
                rawDto.gender(),
                kmsUtil.decrypt(rawDto.birthDate()),
                kmsUtil.decrypt(rawDto.passportNumber()),
                kmsUtil.decrypt(rawDto.passportExpireDate())
        );
    }

}
