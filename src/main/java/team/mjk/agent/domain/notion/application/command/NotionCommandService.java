package team.mjk.agent.domain.notion.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.company.domain.CompanyRepository;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.notion.application.dto.request.NotionConfigSaveServiceRequest;
import team.mjk.agent.domain.notion.application.dto.request.NotionConfigUpdateServiceRequest;
import team.mjk.agent.domain.notion.domain.Notion;
import team.mjk.agent.domain.notion.domain.NotionRepository;
import team.mjk.agent.domain.notion.dto.request.NotionTokenUpdateRequest;
import team.mjk.agent.global.util.KmsUtil;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NotionCommandService {

    private final NotionRepository notionRepository;
    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final KmsUtil kmsUtil;

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

}
