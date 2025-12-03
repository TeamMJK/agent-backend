package team.mjk.agent.domain.slack.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SlackSaveRequest(
    @NotBlank(message = "token 값을 입력해주세요.")
    String token,

    @NotBlank(message = "출장 채널 ID를 입력해주세요. T로 시작합니다.")
    String channelId
) {

}
