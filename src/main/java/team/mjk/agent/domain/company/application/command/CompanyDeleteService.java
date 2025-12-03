package team.mjk.agent.domain.company.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.businessTrip.domain.BusinessTripRepository;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.notion.domain.NotionRepository;
import team.mjk.agent.domain.receipt.application.command.ReceiptCommandService;
import team.mjk.agent.domain.receipt.application.query.ReceiptQueryService;
import team.mjk.agent.domain.slack.domain.SlackRepository;

@RequiredArgsConstructor
@Service
public class CompanyDeleteService {

    private final ReceiptCommandService receiptCommandService;
    private final ReceiptQueryService receiptQueryService;
    private final BusinessTripRepository businessTripRepository;
    private final MemberRepository memberRepository;
    private final NotionRepository notionRepository;
    private final SlackRepository slackRepository;

    @Transactional
    public void deleteCompany(Company company, Long memberId) {
        receiptQueryService.getReceiptsByCompany(company)
                .forEach(receipt -> receiptCommandService.deleteReceipt(memberId, receipt.getId()));
        businessTripRepository.deleteAllByCompanyId(company.getId());
        notionRepository.deleteAllByCompanyId(company.getId());
        slackRepository.deleteAllByCompanyId(company.getId());
        memberRepository.nullifyMembersCompanyByCompanyId(company.getId());
    }

}
