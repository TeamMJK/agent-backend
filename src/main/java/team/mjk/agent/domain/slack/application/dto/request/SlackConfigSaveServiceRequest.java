package team.mjk.agent.domain.slack.application.dto.request;

import lombok.Builder;

@Builder
public record SlackConfigSaveServiceRequest(

        Long companyId,

        String token,

        String channelId

) {
}
