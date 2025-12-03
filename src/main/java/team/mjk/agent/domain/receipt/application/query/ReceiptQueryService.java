package team.mjk.agent.domain.receipt.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.receipt.domain.Receipt;
import team.mjk.agent.domain.receipt.domain.ReceiptRepository;
import team.mjk.agent.domain.receipt.application.dto.response.ReceiptGetResponse;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReceiptQueryService {

    private final ReceiptRepository receiptRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<ReceiptGetResponse> getAllReceipt(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId);
        Company company = member.getValidatedCompany();

        return receiptRepository.findAllByCompany(company).stream()
                .map(ReceiptGetResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ReceiptGetResponse getReceipt(Long memberId, Long receiptId) {
        Member member = memberRepository.findByMemberId(memberId);
        Company company = member.getValidatedCompany();
        Receipt receipt = receiptRepository.findByIdAndCompany(receiptId, company);
        return ReceiptGetResponse.from(receipt);
    }


    @Transactional(readOnly = true)
    public List<Receipt> getReceiptsByCompany(Company company) {
        return receiptRepository.findAllByCompany(company);
    }

}
