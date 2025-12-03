package team.mjk.agent.domain.company.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.company.domain.CompanyRepository;
import team.mjk.agent.domain.company.application.dto.response.CompanyInfoResponse;
import team.mjk.agent.domain.company.application.dto.response.CompanyMemberListResponse;
import team.mjk.agent.domain.companyworkspace.application.query.CompanyWorkspaceQueryService;
import team.mjk.agent.domain.companyworkspace.domain.Workspace;
import team.mjk.agent.domain.member.application.dto.response.MemberInfoGetResponse;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.global.util.KmsUtil;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CompanyQueryService {

    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final CompanyWorkspaceQueryService companyWorkspaceQueryService;
    private final KmsUtil kmsUtil;

    @Transactional(readOnly = true)
    public CompanyInfoResponse getCompanyInfo(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId);
        Long companyId = member.getCompany().getId();
        Company company = companyRepository.findByCompanyId(companyId);

        List<Workspace> workspaces = companyWorkspaceQueryService.getWorkspacesByCompanyId(companyId);

        return CompanyInfoResponse.from(company, workspaces);
    }

    @Transactional(readOnly = true)
    public CompanyMemberListResponse getMembersInfo(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId);

        Company company = member.getValidatedCompany();

        List<Member> members = memberRepository.findAllByCompanyId(company.getId());

        List<MemberInfoGetResponse> memberInfoGetResponses = members.stream()
                .map(m -> {
                    return MemberInfoGetResponse.from(m, kmsUtil);
                })
                .collect(Collectors.toList());

        return CompanyMemberListResponse.builder()
                .members(memberInfoGetResponses)
                .build();
    }

}
