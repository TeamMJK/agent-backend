package team.mjk.agent.domain.businessTrip.application;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.businessTrip.domain.BusinessTrip;
import team.mjk.agent.domain.businessTrip.domain.BusinessTripRepository;
import team.mjk.agent.domain.businessTrip.domain.ServiceType;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripAgentRequest;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripSaveRequest;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripUpdateRequest;
import team.mjk.agent.domain.businessTrip.dto.response.BusinessTripGetAllResponse;
import team.mjk.agent.domain.businessTrip.dto.response.BusinessTripGetResponse;
import team.mjk.agent.domain.businessTrip.dto.response.BusinessTripSaveResponse;
import team.mjk.agent.domain.businessTrip.dto.response.BusinessTripUpdateResponse;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.company.domain.Workspace;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.mcp.McpService;
import team.mjk.agent.domain.mcp.McpServiceRegistry;
import team.mjk.agent.domain.member.presentation.exception.MemberNotFoundException;

@RequiredArgsConstructor
@Service
public class BusinessTripService {

  private final MemberRepository memberRepository;
  private final BusinessTripRepository businessTripRepository;
  private final McpServiceRegistry registry;

  //사용자 직접 저장 메서드
  @Transactional
  public BusinessTripSaveResponse save(Long memberId, BusinessTripSaveRequest request) {
    Member member = memberRepository.findByMemberId(memberId);
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
  public List<Workspace> saveMcp(Long memberId, BusinessTripSaveRequest request) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getValidatedCompany();
    List<Workspace> workspaces = company.getWorkspace();

    for (Workspace workspace : workspaces) {
      List<McpService> mcpServices = registry.getServices(workspace);
      for (McpService mcpService : mcpServices) {
        mcpService.createBusinessTrip(request, company.getId(), member.getName());
      }
    }

    return company.getWorkspace();
  }

  //Agent(저장) -> saveAgentMcp 호출 -> save 호출
  @Transactional
  public void saveAgentMcp(Long memberId, BusinessTripAgentRequest request) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getValidatedCompany();
    List<Workspace> workspaces = company.getWorkspace();

    for (Workspace workspace : workspaces) {
      List<McpService> mcpServices = registry.getServices(workspace);
      for (McpService mcpService : mcpServices) {
        mcpService.createBusinessTripAgent(request, company.getId(), member.getName());
      }
    }

  }

  @Transactional(readOnly = true)
  public BusinessTripGetResponse getBusinessTrip(Long memberId, Long businessTripId) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getValidatedCompany();

    BusinessTrip businessTrip = businessTripRepository.findByIdAndCompanyId(businessTripId,
        company.getId());

    return BusinessTripGetResponse.builder()
        .departDate(businessTrip.getDepartDate())
        .arriveDate(businessTrip.getArriveDate())
        .destination(businessTrip.getDestination())
        .names(businessTrip.getNames())
        .writer(businessTrip.getWriter())
        .serviceType(businessTrip.getServiceType())
        .build();
  }

  @Transactional
  public BusinessTripUpdateResponse update(
      Long memberId,
      Long businessTripId,
      BusinessTripUpdateRequest request
  ) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getValidatedCompany();

    BusinessTrip businessTrip = businessTripRepository.findByIdAndCompanyId(businessTripId,
        company.getId());
    businessTrip.update(request);

    return BusinessTripUpdateResponse.builder()
        .businessTripId(businessTripId)
        .build();
  }

  @Transactional
  public void delete(
      Long memberId,
      Long businessTripId
  ) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getValidatedCompany();

    BusinessTrip businessTrip = businessTripRepository.findByIdAndCompanyId(businessTripId,
        company.getId());
    businessTripRepository.delete(businessTrip);
  }

  @Transactional(readOnly = true)
  public BusinessTripGetAllResponse getAllBusinessTrip(Long memberId) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getValidatedCompany();

    List<BusinessTrip> businessTripList = businessTripRepository.findAllByCompanyId(
        company.getId());

    return BusinessTripGetAllResponse.builder()
        .businessTripList(businessTripList)
        .build();
  }

}