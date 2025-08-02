package team.mjk.agent.domain.member.dto.response;

import java.time.LocalDate;
import lombok.Builder;

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

}
