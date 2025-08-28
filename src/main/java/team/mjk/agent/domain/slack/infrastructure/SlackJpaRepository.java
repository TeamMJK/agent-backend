package team.mjk.agent.domain.slack.infrastructure;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import team.mjk.agent.domain.slack.domain.Slack;

public interface SlackJpaRepository extends JpaRepository<Slack,Long> {

  Optional<Slack> findByCompanyId(Long companyId);

}