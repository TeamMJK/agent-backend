package team.mjk.agent.domain.mcp.slack.domain;

import java.util.Optional;

public interface SlackRepository {

 Slack save(Slack slack);

 Slack findByCompanyId(Long companyId);

 void delete(Slack slack);

 Optional<Slack> findOptionalByCompanyId(Long companyId);

}