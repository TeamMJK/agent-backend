package team.mjk.agent.domain.company.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import team.mjk.agent.domain.company.domain.Workspace;
import team.mjk.agent.domain.company.domain.WorkspaceConfigs;

public record CompanyUpdateRequest(
    @NotBlank(message = "회사 명을 입력해주세요.")
    String name,

    @NotNull(message = "회사 워크스페이스를 선택해주세요." )
    List<Workspace> workspaces,

    WorkspaceConfigs workspaceConfigs
) {

}