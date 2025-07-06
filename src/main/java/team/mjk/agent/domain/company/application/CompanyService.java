package team.mjk.agent.domain.company.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.company.domain.CompanyRepository;
import team.mjk.agent.domain.company.dto.request.CompanyInvitationCodeRequest;
import team.mjk.agent.domain.company.dto.request.CompanySaveRequest;
import team.mjk.agent.domain.company.presentation.exception.CompanyNotFoundException;
import team.mjk.agent.domain.company.presentation.exception.InvalidInvitationCode;
import team.mjk.agent.domain.invitation.InvitationCodeProvider;
import team.mjk.agent.domain.invitation.domain.Invitation;
import team.mjk.agent.domain.invitation.domain.InvitationRepository;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.member.presentation.exception.MemberNotFoundException;

@RequiredArgsConstructor
@Service
public class CompanyService {

    private final InvitationCodeProvider invitationCodeProvider;
    private final InvitationRepository invitationRepository;
    private final CompanyRepository companyRepository;
    private final MemberRepository memberRepository;

    public Invitation createInvitationCode(Long companyId) {
        // TODO: 커스텀 예외 처리 하세용
        if(companyRepository.findById(companyId).isEmpty()) {
            throw new CompanyNotFoundException();
        }

        return invitationCodeProvider.create(companyId);
    }

    public Long create(CompanySaveRequest request, Long memberId) {
        Member member = memberRepository.findByMemberId(memberId)
            .orElseThrow(MemberNotFoundException::new);

        Company company = new Company(request.name(), member.getName());
        companyRepository.save(company);
        return company.getId();
    }

    @Transactional
    public String join(Long memberId, CompanyInvitationCodeRequest request) {
        Member member = memberRepository.findByMemberId(memberId)
            .orElseThrow(MemberNotFoundException::new);

        Invitation invitation = invitationRepository.findByCode(request.invitationCode())
            .orElseThrow(InvalidInvitationCode::new);

        Long companyId = invitation.getCompanyId();

        Company company = companyRepository.findById(companyId)
            .orElseThrow(CompanyNotFoundException::new);

        member.saveCompany(company);

        invitationRepository.delete(invitation);

        return company.getName();
    }

}