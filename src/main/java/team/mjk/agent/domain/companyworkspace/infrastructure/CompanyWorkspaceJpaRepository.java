package team.mjk.agent.domain.companyworkspace.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import team.mjk.agent.domain.companyworkspace.domain.CompanyWorkspace;

import java.util.List;

public interface CompanyWorkspaceJpaRepository extends JpaRepository<CompanyWorkspace, Long> {

    List<CompanyWorkspace> findAllByCompanyId(Long companyId);

}
