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
import team.mjk.agent.domain.member.presentation.exception.MemberNotFoundException;
import team.mjk.agent.global.mcp.McpService;
import team.mjk.agent.global.mcp.McpServiceRegistry;

@RequiredArgsConstructor
@Service
public class BusinessTripService {

  private final MemberRepository memberRepository;
  private final BusinessTripRepository businessTripRepository;
  private final McpServiceRegistry registry;

  //사용자 직접 저장 메서드
  @Transactional
  public BusinessTripSaveResponse save(Long memberId, BusinessTripSaveRequest request) {
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(MemberNotFoundException::new);

    BusinessTrip businessTrip = BusinessTrip.create(
        request.departDate(),
        request.arriveDate(),
        request.destination(),
        request.names(),
        member.getName(),
        member.getCompany().getId(),
        request.serviceType()
    );
    businessTripRepository.save(businessTrip);

    return BusinessTripSaveResponse.builder()
        .businessTripId(businessTrip.getId())
        .build();
  }

  //Agent 통한 저장 메서드
  @Transactional
  public BusinessTripSaveResponse save(Long memberId, BusinessTripAgentRequest request) {
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(MemberNotFoundException::new);

    BusinessTrip businessTrip = BusinessTrip.create(
        LocalDate.parse(request.departDate()),
        LocalDate.parse(request.arriveDate()),
        request.destination(),
        request.names(),
        member.getName(),
        member.getCompany().getId(),
        ServiceType.fromCategory(request.serviceType())
    );
    businessTripRepository.save(businessTrip);

    return BusinessTripSaveResponse.builder()
        .businessTripId(businessTrip.getId())
        .build();
  }

  //Controller(저장) -> saveMcp 호출 -> save 호출
  @Transactional
  public Workspace saveMcp(Long memberId, BusinessTripSaveRequest request) {
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(MemberNotFoundException::new);

    Company company = member.getCompany();
    Workspace workspace = company.getWorkspace();
    if (workspace == Workspace.NONE){
      save(memberId, request);
      return workspace;
    }

    McpService mcpService = registry.getService(workspace);
    mcpService.createBusinessTrip(request, company.getId());

    return company.getWorkspace();
  }

  //Agent(저장) -> saveAgentMcp 호출 -> save 호출
  @Transactional
  public void saveAgentMcp(Long memberId, BusinessTripAgentRequest request) {
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(MemberNotFoundException::new);

    Company company = member.getCompany();
    Workspace workspace = company.getWorkspace();
    if (workspace == Workspace.NONE){
      save(memberId, request);
      return;
    }

    McpService mcpService = registry.getService(workspace);
    mcpService.createBusinessTripAgent(request, company.getId());
  }

  @Transactional(readOnly = true)
  public BusinessTripGetResponse getBusinessTrip(Long memberId, Long businessTripId) {
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(MemberNotFoundException::new);

    Long companyId = member.getCompany().getId();

    BusinessTrip businessTrip = businessTripRepository.findByIdAndCompanyId(businessTripId,
        companyId);

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
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(MemberNotFoundException::new);

    Long companyId = member.getCompany().getId();

    BusinessTrip businessTrip = businessTripRepository.findByIdAndCompanyId(businessTripId,
        companyId);
    businessTrip.update(request);

    return BusinessTripUpdateResponse.builder()
        .businessTripId(businessTripId)
        .build();
  }

  @Transactional(readOnly = true)
  public BusinessTripGetAllResponse getAllBusinessTrip(Long memberId) {
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(MemberNotFoundException::new);

    Long companyId = member.getCompany().getId();

    List<BusinessTrip> businessTripList = businessTripRepository.findAllByCompanyId(companyId);

    return BusinessTripGetAllResponse.builder()
        .businessTripList(businessTripList)
        .build();
  }

}