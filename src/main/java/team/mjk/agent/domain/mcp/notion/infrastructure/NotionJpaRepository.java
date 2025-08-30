package team.mjk.agent.domain.mcp.notion.infrastructure;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import team.mjk.agent.domain.mcp.notion.domain.Notion;

public interface NotionJpaRepository extends JpaRepository<Notion,Long> {

  Optional<Notion> findByCompanyId(Long companyId);
}
