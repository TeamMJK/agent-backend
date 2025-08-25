package team.mjk.agent.domain.member.dto.response;

import lombok.Builder;

@Builder
public record EncryptedMemberInfoResponse(

        String name,

        String firstName,

        String lastName,

        String phoneNumber,

        String gender,

        String birthDate,

        String passportNumber,

        String passportExpireDate

) {
}
