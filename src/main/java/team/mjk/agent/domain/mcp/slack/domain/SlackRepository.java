package team.mjk.agent.domain.mcp.slack.domain;

public interface SlackRepository {

 Slack save(Slack slack);

 Slack findByCompanyId(Long companyId);

 void delete(Slack slack);

}