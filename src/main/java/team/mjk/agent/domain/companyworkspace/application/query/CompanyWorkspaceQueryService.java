package team.mjk.agent.domain.companyworkspace.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.companyworkspace.domain.CompanyWorkspace;
import team.mjk.agent.domain.companyworkspace.domain.CompanyWorkspaceRepository;
import team.mjk.agent.domain.companyworkspace.domain.Workspace;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CompanyWorkspaceQueryService {

    private final CompanyWorkspaceRepository companyWorkspaceRepository;

    @Transactional(readOnly = true)
    public List<Workspace> getWorkspacesByCompanyId(Long companyId) {
        return companyWorkspaceRepository.findAllByCompanyId(companyId).stream()
                .map(CompanyWorkspace::getWorkspace)
                .toList();
    }

}
