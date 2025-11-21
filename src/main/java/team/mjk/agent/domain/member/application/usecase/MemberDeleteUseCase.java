package team.mjk.agent.domain.member.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.company.domain.CompanyRepository;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;

@RequiredArgsConstructor
@Service
public class MemberDeleteUseCase {

    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public Long delete(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId);
        Company company = member.getCompany();

        if (company != null) {
            long remainCount = memberRepository.countByCompanyId(company.getId());
            if (remainCount == 0) {
                companyRepository.delete(company);
            }
        }

        memberRepository.delete(memberId);

        return memberId;
    }

}
