package team.mjk.agent.domain.company.dto.response;

import java.util.List;
import lombok.Builder;
import team.mjk.agent.domain.company.domain.Workspace;

@Builder
public record CompanyInfoResponse(
    String name,
    List<Workspace> workspaces
) {

}
