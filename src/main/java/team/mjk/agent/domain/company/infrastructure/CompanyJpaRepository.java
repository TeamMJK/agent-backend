package team.mjk.agent.domain.company.infrastructure;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import team.mjk.agent.domain.company.domain.Company;

public interface CompanyJpaRepository extends JpaRepository<Company, Long> {

}