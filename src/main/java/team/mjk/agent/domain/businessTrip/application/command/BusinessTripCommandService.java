package team.mjk.agent.domain.businessTrip.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.businessTrip.application.dto.request.BusinessTripSaveServiceRequest;
import team.mjk.agent.domain.businessTrip.application.dto.response.BusinessTripSaveResponse;
import team.mjk.agent.domain.businessTrip.domain.BusinessTrip;
import team.mjk.agent.domain.businessTrip.domain.BusinessTripRepository;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.companyworkspace.application.query.CompanyWorkspaceQueryService;
import team.mjk.agent.domain.companyworkspace.domain.Workspace;
import team.mjk.agent.domain.mcp.McpService;
import team.mjk.agent.domain.mcp.McpServiceRegistry;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BusinessTripCommandService {

    private final BusinessTripRepository businessTripRepository;
    private final MemberRepository memberRepository;
    private final CompanyWorkspaceQueryService companyWorkspaceQueryService;
    private final McpServiceRegistry mcpServiceRegistry;

    @Transactional
    public BusinessTripSaveResponse save(BusinessTripSaveServiceRequest request) {
        Member member = memberRepository.findByMemberId(request.memberId());
        Company company = member.getValidatedCompany();

        BusinessTrip businessTrip = BusinessTrip.create(
                request.departDate(),
                request.arriveDate(),
                request.destination(),
                request.names(),
                member.getName(),
                company.getId(),
                request.serviceType()
        );
        businessTripRepository.save(businessTrip);

        return BusinessTripSaveResponse.builder()
                .businessTripId(businessTrip.getId())
                .build();
    }

    //Controller(저장) -> saveMcp 호출 -> save 호출
    @Transactional
    public List<Workspace> saveMcp(BusinessTripSaveServiceRequest request) {
        Member member = memberRepository.findByMemberId(request.memberId());
        Company company = member.getValidatedCompany();
        List<Workspace> workspaces = companyWorkspaceQueryService.getWorkspacesByCompanyId(company.getId());

        for (Workspace workspace : workspaces) {
            List<McpService> mcpServices = mcpServiceRegistry.getServices(workspace);
            for (McpService mcpService : mcpServices) {
                mcpService.createBusinessTrip(request, company.getId(), member.getName());
            }
        }

        return workspaces;
    }

}
