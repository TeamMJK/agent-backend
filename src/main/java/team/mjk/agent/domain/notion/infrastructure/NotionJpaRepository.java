package team.mjk.agent.domain.notion.infrastructure;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import team.mjk.agent.domain.notion.domain.Notion;

public interface NotionJpaRepository extends JpaRepository<Notion,Long> {

  Optional<Notion> findByCompanyId(Long companyId);
}
