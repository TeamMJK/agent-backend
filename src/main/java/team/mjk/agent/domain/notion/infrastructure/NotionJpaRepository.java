package team.mjk.agent.domain.notion.infrastructure;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.mjk.agent.domain.notion.domain.Notion;

public interface NotionJpaRepository extends JpaRepository<Notion,Long> {

  Optional<Notion> findByCompanyId(Long companyId);

  @Modifying
  @Query("delete from Notion n where n.companyId = :companyId")
  void deleteAllByCompanyId(@Param("companyId") Long companyId);
}
