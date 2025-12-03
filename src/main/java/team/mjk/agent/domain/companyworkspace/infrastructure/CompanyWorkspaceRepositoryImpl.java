package team.mjk.agent.domain.companyworkspace.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.mjk.agent.domain.companyworkspace.domain.CompanyWorkspace;
import team.mjk.agent.domain.companyworkspace.domain.CompanyWorkspaceRepository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CompanyWorkspaceRepositoryImpl implements CompanyWorkspaceRepository {

    private final CompanyWorkspaceJpaRepository companyWorkspaceJpaRepository;

    @Override
    public void save(CompanyWorkspace companyWorkspace) {
        companyWorkspaceJpaRepository.save(companyWorkspace);
    }

    @Override
    public void delete(CompanyWorkspace companyWorkspace) {
        companyWorkspaceJpaRepository.delete(companyWorkspace);
    }

    @Override
    public List<CompanyWorkspace> findAllByCompanyId(Long companyId) {
        return companyWorkspaceJpaRepository.findAllByCompanyId(companyId);
    }

}
