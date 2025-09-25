package team.mjk.agent.domain.mcp.slack.infrastructure;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.mjk.agent.domain.mcp.slack.domain.Slack;

public interface SlackJpaRepository extends JpaRepository<Slack,Long> {

  Optional<Slack> findByCompanyId(Long companyId);

  @Modifying
  @Query("delete from Slack s where s.companyId = :companyId")
  void deleteAllByCompanyId(@Param("companyId") Long companyId);

}