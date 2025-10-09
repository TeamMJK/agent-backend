package team.mjk.agent.domain.passport.presentation.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import team.mjk.agent.domain.passport.application.dto.request.PassportUpdateServiceRequest;

@Builder
public record PassportUpdateRequest(

        @NotBlank(message = "여권 번호를 입력해 주세요.")
        String passportNumber,

        @NotBlank(message = "여권 만료일을 입력해 주세요.")
        String passportExpireDate

) {

    public PassportUpdateServiceRequest toServiceRequest(Long memberId) {
        return PassportUpdateServiceRequest.builder()
                .memberId(memberId)
                .passportNumber(passportNumber)
                .passportExpireDate(passportExpireDate)
                .build();
    }

}
