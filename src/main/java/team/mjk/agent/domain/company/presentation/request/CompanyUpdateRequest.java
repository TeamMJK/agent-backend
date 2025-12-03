package team.mjk.agent.domain.company.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import team.mjk.agent.domain.company.application.dto.request.CompanyUpdateServiceRequest;
import team.mjk.agent.domain.companyworkspace.domain.Workspace;
import team.mjk.agent.domain.notion.presentation.request.NotionConfigUpdateRequest;
import team.mjk.agent.domain.slack.presentation.request.SlackConfigSaveRequest;
import team.mjk.agent.domain.slack.presentation.request.SlackConfigUpdateRequest;

import java.util.List;

public record CompanyUpdateRequest(

        @NotBlank(message = "회사 명을 입력해주세요.")
        String name,

        @NotNull(message = "회사 워크스페이스를 선택해주세요." )
        List<Workspace> workspaces,

//        @NotNull(message = "노션 기본 정보들을 입력해 주세요.")
        NotionConfigUpdateRequest notionConfigUpdateRequest,

//        @NotNull(message = "슬랙 기본 정보들을 입력해 주세요.")
        SlackConfigUpdateRequest slackConfigUpdateRequest

) {

    public CompanyUpdateServiceRequest toServiceRequest(Long memberId) {
        return CompanyUpdateServiceRequest.builder()
                .memberId(memberId)
                .name(name)
                .workspaces(workspaces)
                .notionConfigUpdateRequest(notionConfigUpdateRequest)
                .slackConfigUpdateRequest(slackConfigUpdateRequest)
                .build();
    }

}
