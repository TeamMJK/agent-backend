package team.mjk.agent.domain.company.presentation.request;

import jakarta.validation.constraints.NotBlank;
import team.mjk.agent.domain.company.application.dto.request.CompanyInvitationCodeServiceRequest;

public record CompanyInvitationCodeRequest(

        @NotBlank(message = "초대 코드를 입력해 주세요.")
        String invitationCode
) {

    public CompanyInvitationCodeServiceRequest toServiceRequest(Long memberId) {
        return CompanyInvitationCodeServiceRequest.builder()
                .memberId(memberId)
                .invitationCode(invitationCode)
                .build();
    }

}
