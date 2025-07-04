package team.mjk.agent.domain.passport.dto.request;

import java.time.LocalDate;

public record PassportInfoSaveRequest(

    String passportNumber,
    LocalDate passportExpireDate

) {
}