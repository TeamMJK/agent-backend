package team.mjk.agent.domain.company.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import team.mjk.agent.domain.company.domain.Workspace;

public record CompanySaveRequest(
    @NotBlank(message = "회사 명을 입력해주세요.")
    String name,

    @NotNull(message = "회사 워크스페이스를 선택해주세요." )
    Workspace workspace
) {
}