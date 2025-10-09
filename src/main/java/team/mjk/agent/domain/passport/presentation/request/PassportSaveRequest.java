package team.mjk.agent.domain.passport.presentation.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import team.mjk.agent.domain.passport.application.dto.request.PassportSaveServiceRequest;

@Builder
public record PassportSaveRequest(

        @NotBlank(message = "여권 번호를 입력해 주세요.")
        String passportNumber,

        @NotBlank(message = "여권 만료일을 입력해 주세요.")
        String passportExpireDate

) {

    public PassportSaveServiceRequest toServiceRequest(Long memberId) {
        return PassportSaveServiceRequest.builder()
                .passportNumber(passportNumber)
                .passportExpireDate(passportExpireDate)
                .memberId(memberId)
                .build();
    }

}
