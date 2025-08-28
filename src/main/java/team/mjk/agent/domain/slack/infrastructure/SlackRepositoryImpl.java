package team.mjk.agent.domain.slack.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.mjk.agent.domain.slack.domain.Slack;
import team.mjk.agent.domain.slack.domain.SlackRepository;
import team.mjk.agent.domain.slack.presentation.exception.SlackNotFoundException;

@RequiredArgsConstructor
@Repository
public class SlackRepositoryImpl implements SlackRepository {

  private final SlackJpaRepository slackJpaRepository;

  @Override
  public Slack save(Slack slack) {
    return slackJpaRepository.save(slack);
  }

  @Override
  public Slack findByCompanyId(Long companyId) {
    return slackJpaRepository.findByCompanyId(companyId)
        .orElseThrow(SlackNotFoundException::new);
  }

  @Override
  public void delete(Slack slack) {
    slackJpaRepository.delete(slack);
  }

}