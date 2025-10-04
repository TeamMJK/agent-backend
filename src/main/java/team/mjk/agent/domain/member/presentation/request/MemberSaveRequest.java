package team.mjk.agent.domain.member.presentation.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import team.mjk.agent.domain.member.application.dto.request.MemberSaveServiceRequest;

@Builder
public record MemberSaveRequest(

        @NotBlank(message = "이메일을 입력해 주세요.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀번호를 입력해 주세요.")
        String password

) {

    public MemberSaveServiceRequest toServiceRequest() {
        return MemberSaveServiceRequest.builder()
                .email(email)
                .password(password)
                .build();
    }

}
