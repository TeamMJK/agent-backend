package team.mjk.agent.domain.company.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import team.mjk.agent.domain.company.application.dto.request.CompanySaveServiceRequest;
import team.mjk.agent.domain.companyworkspace.domain.Workspace;
import team.mjk.agent.domain.notion.presentation.request.NotionConfigSaveRequest;
import team.mjk.agent.domain.slack.presentation.request.SlackConfigSaveRequest;

import java.util.List;

public record CompanySaveRequest(

        @NotBlank(message = "회사 명을 입력해주세요.")
        String name,

        @NotNull(message = "회사 워크스페이스를 선택해주세요." )
        List<Workspace> workspaces,

//        @NotNull(message = "노션 기본 정보들을 입력해 주세요.")
        NotionConfigSaveRequest notionConfigSaveRequest,

//        @NotNull(message = "슬랙 기본 정보들을 입력해 주세요.")
        SlackConfigSaveRequest slackConfigSaveRequest

) {

    public CompanySaveServiceRequest toServiceRequest(Long memberId) {
        return CompanySaveServiceRequest.builder()
                .memberId(memberId)
                .name(name)
                .workspaces(workspaces)
                .notionConfigSaveRequest(notionConfigSaveRequest)
                .slackConfigSaveRequest(slackConfigSaveRequest)
                .build();
    }

}
