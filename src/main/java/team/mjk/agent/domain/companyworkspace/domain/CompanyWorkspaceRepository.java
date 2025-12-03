package team.mjk.agent.domain.companyworkspace.domain;

import java.util.List;

public interface CompanyWorkspaceRepository {

    void save(CompanyWorkspace companyWorkspace);

    void delete(CompanyWorkspace companyWorkspace);

    List<CompanyWorkspace> findAllByCompanyId(Long companyId);

}
