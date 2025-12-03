package team.mjk.agent.domain.company.application.dto.request;

import lombok.Builder;
import team.mjk.agent.domain.companyworkspace.domain.Workspace;
import team.mjk.agent.domain.notion.presentation.request.NotionConfigSaveRequest;
import team.mjk.agent.domain.slack.presentation.request.SlackConfigSaveRequest;

import java.util.List;

@Builder
public record CompanySaveServiceRequest(

        Long memberId,

        String name,

        List<Workspace> workspaces,

        NotionConfigSaveRequest notionConfigSaveRequest,

        SlackConfigSaveRequest slackConfigSaveRequest

) {
}
