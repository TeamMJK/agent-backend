package team.mjk.agent.domain.notion.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.businessTrip.application.dto.request.BusinessTripSaveServiceRequest;
import team.mjk.agent.domain.businessTrip.presentation.request.BusinessTripAgentRequest;
import team.mjk.agent.domain.businessTrip.presentation.request.BusinessTripSaveRequest;
import team.mjk.agent.domain.companyworkspace.domain.Workspace;
import team.mjk.agent.domain.mcp.McpService;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.notion.application.dto.request.NotionConfigSaveServiceRequest;
import team.mjk.agent.domain.notion.application.dto.request.NotionConfigUpdateServiceRequest;
import team.mjk.agent.domain.notion.domain.Notion;
import team.mjk.agent.domain.notion.domain.NotionRepository;
import team.mjk.agent.domain.notion.infrastructure.NotionPayloadFactory;
import team.mjk.agent.domain.notion.infrastructure.NotionProvider;
import team.mjk.agent.domain.receipt.presentation.request.ReceiptMcpRequest;
import team.mjk.agent.global.util.KmsUtil;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NotionCommandService implements McpService {

    private final NotionRepository notionRepository;
    private final KmsUtil kmsUtil;
    private final NotionProvider notionProvider;
    private final NotionPayloadFactory notionPayloadFactory;

    @Transactional
    public void saveNotion(NotionConfigSaveServiceRequest request) {
        Notion notion = Notion.create(
                kmsUtil.encrypt(request.token()),
                kmsUtil.encrypt(request.businessTripDatabaseId()),
                kmsUtil.encrypt(request.receiptDatabaseId()),
                request.companyId()
        );
        notionRepository.save(notion);
    }

    @Transactional
    public void updateNotion(NotionConfigUpdateServiceRequest request) {
        Optional<Notion> notionOptional = notionRepository.findOptionalByCompanyId(request.companyId());

        if (notionOptional.isPresent()) {
            Notion notion = notionOptional.get();
            notion.update(
                    request.token(),
                    request.businessTripDatabaseId(),
                    request.receiptDatabaseId(),
                    kmsUtil
            );
        } else {
            Notion notion = Notion.create(
                    kmsUtil.encrypt(request.token()),
                    kmsUtil.encrypt(request.businessTripDatabaseId()),
                    kmsUtil.encrypt(request.receiptDatabaseId()),
                    request.companyId()
            );
            notionRepository.save(notion);
        }
    }

    @Override
    public void createBusinessTrip(BusinessTripSaveServiceRequest request, Long companyId, String requester) {
        Notion notion = notionRepository.findByCompanyId(companyId);

        Map<String, Object> payload =
                notionPayloadFactory.businessTrip(request, notion, requester);

        notionProvider.send(payload, notion, kmsUtil.decrypt(notion.getToken()));
    }

    @Override
    public void createBusinessTripAgent(BusinessTripAgentRequest request, Long companyId, String requester) {
        Notion notion = notionRepository.findByCompanyId(companyId);

        Map<String, Object> payload =
                notionPayloadFactory.businessTripAgent(request, notion, requester);

        notionProvider.send(payload, notion, kmsUtil.decrypt(notion.getToken()));
    }

    @Override
    public void createReceipt(ReceiptMcpRequest request, Long companyId, Member member) {
        Notion notion = notionRepository.findByCompanyId(companyId);

        Map<String, Object> payload =
                notionPayloadFactory.receipt(request, notion, member);

        notionProvider.send(payload, notion, kmsUtil.decrypt(notion.getToken()));
    }

    @Override
    public Workspace getWorkspace() {
        return Workspace.NOTION;
    }

}
