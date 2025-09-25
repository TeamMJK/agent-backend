package team.mjk.agent.domain.company.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.businessTrip.domain.BusinessTripRepository;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.receipt.application.ReceiptService;

@RequiredArgsConstructor
@Service
public class CompanyCleanupService {

    private final ReceiptService receiptService;
    private final BusinessTripRepository businessTripRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void cleanupCompany(Company company, Long memberId) {
        receiptService.getReceiptsByCompany(company)
                .forEach(receipt -> receiptService.deleteReceipt(memberId, receipt.getId()));
        businessTripRepository.deleteAllByCompanyId(company.getId());
        memberRepository.nullifyMembersCompanyByCompanyId(company.getId());
    }

}
