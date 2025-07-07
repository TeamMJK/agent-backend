package team.mjk.agent.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record MemberInfoUpdateRequest(

    @NotBlank(message = "이름을 입력해주세요.")
    String name,

    @NotBlank(message = "영문 성을 입력해주세요.")
    String firstName,

    @NotBlank(message = "영문 이름을 입력해주세요.")
    String lastName,

    @NotBlank(message = "핸드폰 번호를 입력해주세요.")
    String phoneNumber,

    @NotBlank(message = "성별을 선택해주세요.")
    String gender,

    @NotNull(message = "생일을 선택해주세요.")
    LocalDate birthDate,

    @NotBlank(message = "여권 번호를 입력해주세요.")
    String passportNumber,

    @NotNull(message = "여권 만료일을 선택해주세요.")
    LocalDate passportExpireDate
) {
}