package team.mjk.agent.domain.member.application.dto.response;

import lombok.Builder;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.global.util.KmsUtil;

@Builder
public record MemberInfoGetResponse(
    String name,
    String email,
    String firstName,
    String lastName,
    String phoneNumber,
    String gender,
    String birthDate,

    String passportNumber,
    String passportExpireDate
) {

    public static MemberInfoGetResponse from(Member member, KmsUtil kmsUtil) {
        return MemberInfoGetResponse.builder()
                .name(member.getName())
                .firstName(kmsUtil.decrypt(member.getFirstName()))
                .lastName(kmsUtil.decrypt(member.getLastName()))
                .email(member.getEmail())
                .phoneNumber(kmsUtil.decrypt(member.getPhoneNumber()))
                .gender(member.getGender() != null ? member.getGender().name() : null)
                .birthDate(kmsUtil.decrypt(member.getBirthDate()))
                .passportNumber(kmsUtil.decrypt(member.getPassport().getPassportNumber()))
                .passportExpireDate(kmsUtil.decrypt(member.getPassport().getPassportExpireDate()))
                .build();
    }

}
