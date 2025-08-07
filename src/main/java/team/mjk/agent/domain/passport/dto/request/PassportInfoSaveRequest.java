package team.mjk.agent.domain.passport.dto.request;

public record PassportInfoSaveRequest(

        String passportNumber,
        String passportExpireDate

) {
}