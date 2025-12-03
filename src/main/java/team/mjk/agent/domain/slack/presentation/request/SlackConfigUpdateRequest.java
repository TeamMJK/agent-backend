package team.mjk.agent.domain.slack.presentation.request;

import jakarta.validation.constraints.NotBlank;
import team.mjk.agent.domain.slack.application.dto.request.SlackConfigUpdateServiceRequest;

public record SlackConfigUpdateRequest(

        @NotBlank(message = "token 값을 입력해주세요.")
        String token,

        @NotBlank(message = "출장 채널 ID를 입력해주세요. T로 시작합니다.")
        String channelId

) {

    public SlackConfigUpdateServiceRequest toServiceRequest(Long companyId) {
        return SlackConfigUpdateServiceRequest.builder()
                .companyId(companyId)
                .token(token)
                .channelId(channelId)
                .build();
    }

}
