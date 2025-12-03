package team.mjk.agent.domain.company.application.dto.response;

import java.util.List;

import lombok.Builder;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.companyworkspace.domain.Workspace;

@Builder
public record CompanyInfoResponse(

        String name,

        List<Workspace> workspaces

) {

    public static CompanyInfoResponse from(Company company, List<Workspace> workspaces) {
        return CompanyInfoResponse.builder()
                .name(company.getName())
                .workspaces(workspaces)
                .build();
    }

}
