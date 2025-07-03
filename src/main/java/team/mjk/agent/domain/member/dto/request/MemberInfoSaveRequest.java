package team.mjk.agent.domain.member.dto.request;

import java.time.LocalDate;

public record MemberInfoSaveRequest(

        String name,
        String firstName,
        String lastName,
        String phoneNumber,
        String gender,
        LocalDate birthDate

) {
}
