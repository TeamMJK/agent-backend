package team.mjk.agent.domain.slack.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.slack.application.dto.request.SlackConfigSaveServiceRequest;
import team.mjk.agent.domain.slack.application.dto.request.SlackConfigUpdateServiceRequest;
import team.mjk.agent.domain.slack.domain.Slack;
import team.mjk.agent.domain.slack.domain.SlackRepository;
import team.mjk.agent.global.util.KmsUtil;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SlackCommandService {

    private final SlackRepository slackRepository;
    private final KmsUtil kmsUtil;

    @Transactional
    public void saveSlack(SlackConfigSaveServiceRequest request) {
        Slack slack = Slack.create(
                kmsUtil.encrypt(request.token()),
                kmsUtil.encrypt(request.channelId()),
                request.companyId()
        );
        slackRepository.save(slack);
    }

    @Transactional
    public void updateSlack(SlackConfigUpdateServiceRequest request) {
        Optional<Slack> slackOptional = slackRepository.findOptionalByCompanyId(request.companyId());

        if (slackOptional.isPresent()) {
            Slack slack = slackOptional.get();
            slack.update(
                    request.token(),
                    request.channelId(),
                    kmsUtil
            );
        } else {
            Slack slack = Slack.create(
                    kmsUtil.encrypt(request.token()),
                    kmsUtil.encrypt(request.channelId()),
                    request.companyId()
            );
            slackRepository.save(slack);
        }
    }

}
