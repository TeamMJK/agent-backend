package team.mjk.agent.domain.company.presentation.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import team.mjk.agent.domain.company.application.dto.request.CompanyInvitationEmailServiceRequest;

public record CompanyInvitationEmailRequest(

        @NotBlank(message = "이메일을 입력해 주세요.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email

) {

    public CompanyInvitationEmailServiceRequest toServiceRequest(Long memberId) {
        return CompanyInvitationEmailServiceRequest.builder()
                .memberId(memberId)
                .email(email)
                .build();
    }

}
