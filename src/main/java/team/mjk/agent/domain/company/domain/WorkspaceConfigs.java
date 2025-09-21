package team.mjk.agent.domain.company.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import team.mjk.agent.domain.mcp.notion.dto.request.NotionTokenRequest;
import team.mjk.agent.domain.mcp.slack.dto.request.SlackSaveRequest;

@Getter
public class WorkspaceConfigs {

  @JsonProperty("NOTION")
  private NotionTokenRequest notionTokenRequest;

  @JsonProperty("SLACK")
  private SlackSaveRequest slackSaveRequest;

}