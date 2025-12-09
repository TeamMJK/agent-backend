package team.mjk.agent.domain.slack.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripAgentRequest;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripSaveRequest;
import team.mjk.agent.domain.companyworkspace.domain.Workspace;
import team.mjk.agent.domain.mcp.McpService;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.receipt.presentation.request.ReceiptMcpRequest;
import team.mjk.agent.domain.slack.application.dto.request.SlackConfigSaveServiceRequest;
import team.mjk.agent.domain.slack.application.dto.request.SlackConfigUpdateServiceRequest;
import team.mjk.agent.domain.slack.domain.Slack;
import team.mjk.agent.domain.slack.domain.SlackRepository;
import team.mjk.agent.domain.slack.infrastructure.SlackPayloadFactory;
import team.mjk.agent.domain.slack.infrastructure.SlackProvider;
import team.mjk.agent.global.util.KmsUtil;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SlackCommandService implements McpService {

    private final SlackRepository slackRepository;
    private final SlackPayloadFactory payloadFactory;
    private final SlackProvider slackProvider;
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

    @Override
    public void createBusinessTrip(BusinessTripSaveRequest request, Long companyId, String requester) {
        Slack slack = slackRepository.findByCompanyId(companyId);
        String token = kmsUtil.decrypt(slack.getToken());
        String channelId = kmsUtil.decrypt(slack.getChannelId());

        String message = payloadFactory.businessTrip(request, requester);

        slackProvider.sendMessage(token, channelId, message);
    }

    @Override
    public void createBusinessTripAgent(BusinessTripAgentRequest request, Long companyId, String requester) {
        Slack slack = slackRepository.findByCompanyId(companyId);
        String token = kmsUtil.decrypt(slack.getToken());
        String channelId = kmsUtil.decrypt(slack.getChannelId());

        String message = payloadFactory.businessTripAgent(request, requester);

        slackProvider.sendMessage(token, channelId, message);
    }

    @Override
    public void createReceipt(ReceiptMcpRequest req, Long companyId, Member member) {
        Slack slack = slackRepository.findByCompanyId(companyId);
        String token = kmsUtil.decrypt(slack.getToken());
        String channelId = kmsUtil.decrypt(slack.getChannelId());

        String message = payloadFactory.receipt(req, member);

        slackProvider.sendMessage(token, channelId, message);
    }

    @Override
    public Workspace getWorkspace() {
        return Workspace.SLACK;
    }

}
