package team.mjk.agent.domain.company.application.dto.request;

import lombok.Builder;
import team.mjk.agent.domain.companyworkspace.domain.Workspace;
import team.mjk.agent.domain.notion.presentation.request.NotionConfigUpdateRequest;
import team.mjk.agent.domain.slack.presentation.request.SlackConfigUpdateRequest;

import java.util.List;

@Builder
public record CompanyUpdateServiceRequest(

        Long memberId,

        String name,

        List<Workspace> workspaces,

        NotionConfigUpdateRequest notionConfigUpdateRequest,

        SlackConfigUpdateRequest slackConfigUpdateRequest

) {
}
