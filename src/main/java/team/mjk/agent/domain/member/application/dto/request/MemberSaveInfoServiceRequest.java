package team.mjk.agent.domain.member.application.dto.request;

import lombok.Builder;

@Builder
public record MemberSaveInfoServiceRequest(

        Long memberId,

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
