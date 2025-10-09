package team.mjk.agent.domain.member.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import team.mjk.agent.domain.member.application.dto.request.MemberSaveInfoServiceRequest;

@Builder
public record MemberSaveInfoRequest(

        @NotBlank(message = "이름을 입력해주세요.")
        String name,

        @NotBlank(message = "영문 이름을 입력해주세요.")
        String firstName,

        @NotBlank(message = "영문 성을 입력해주세요.")
        String lastName,

        @NotBlank(message = "핸드폰 번호를 입력해주세요.")
        String phoneNumber,

        @NotBlank(message = "성별을 선택해주세요.")
        String gender,

        @NotNull(message = "생일을 선택해주세요.")
        String birthDate

) {

    public MemberSaveInfoServiceRequest toServiceRequest(Long memberId) {
        return MemberSaveInfoServiceRequest.builder()
                .memberId(memberId)
                .name(name)
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .gender(gender)
                .birthDate(birthDate)
                .build();
    }

}
