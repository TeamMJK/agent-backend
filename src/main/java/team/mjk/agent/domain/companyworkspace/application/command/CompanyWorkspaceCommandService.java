package team.mjk.agent.domain.companyworkspace.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.companyworkspace.domain.CompanyWorkspace;
import team.mjk.agent.domain.companyworkspace.domain.CompanyWorkspaceRepository;
import team.mjk.agent.domain.companyworkspace.domain.Workspace;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CompanyWorkspaceCommandService {

    private final CompanyWorkspaceRepository companyWorkspaceRepository;

    @Transactional
    public void createWorkspaces(Company company, List<Workspace> workspaceTypes) {
        for (Workspace workspace : workspaceTypes) {
            CompanyWorkspace companyWorkspace = CompanyWorkspace.create(workspace, company);
            companyWorkspaceRepository.save(companyWorkspace);
        }
    }

    @Transactional
    public void updateWorkspaces(Company company, List<Workspace> newWorkspaces) {
        List<CompanyWorkspace> existingWorkspaces = companyWorkspaceRepository.findAllByCompanyId(company.getId());

        for (CompanyWorkspace companyWorkspace : existingWorkspaces) {
            if (!newWorkspaces.contains(companyWorkspace.getWorkspace())) {
                companyWorkspaceRepository.delete(companyWorkspace);
            }
        }

        for (Workspace wsType : newWorkspaces) {
            boolean exists = existingWorkspaces.stream()
                    .anyMatch(ws -> ws.getWorkspace() == wsType);
            if (!exists) {
                CompanyWorkspace newWs = CompanyWorkspace.create(wsType, company);
                companyWorkspaceRepository.save(newWs);
            }
        }
    }

}
