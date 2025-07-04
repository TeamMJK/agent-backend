package team.mjk.agent.domain.businessTrip.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.businessTrip.domain.BusinessTrip;
import team.mjk.agent.domain.businessTrip.domain.BusinessTripRepository;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripSaveRequest;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripUpdateRequest;
import team.mjk.agent.domain.businessTrip.dto.response.BusinessTripGetAllResponse;
import team.mjk.agent.domain.businessTrip.dto.response.BusinessTripGetResponse;
import team.mjk.agent.domain.businessTrip.dto.response.BusinessTripSaveResponse;
import team.mjk.agent.domain.businessTrip.dto.response.BusinessTripUpdateResponse;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.member.presentation.exception.MemberNotFoundException;

@RequiredArgsConstructor
@Service
public class BusinessTripService {

  private final MemberRepository memberRepository;
  private final BusinessTripRepository businessTripRepository;

  @Transactional
  public BusinessTripSaveResponse save(Long memberId, BusinessTripSaveRequest request) {
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(MemberNotFoundException::new);

    BusinessTrip businessTrip = BusinessTrip.create(
        request.departDate(),
        request.arriveDate(),
        request.destination(),
        request.names(),
        member.getName()
    );
    businessTripRepository.save(businessTrip);

    return BusinessTripSaveResponse.builder()
        .businessTripId(businessTrip.getId())
        .build();
  }

  @Transactional(readOnly = true)
  public BusinessTripGetResponse getBusinessTrip(Long memberId, Long businessTripId) {
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(MemberNotFoundException::new);

    BusinessTrip businessTrip = businessTripRepository.findById(businessTripId);
    return BusinessTripGetResponse.builder()
        .departDate(businessTrip.getDepartDate())
        .arriveDate(businessTrip.getArriveDate())
        .destination(businessTrip.getDestination())
        .names(businessTrip.getNames())
        .writer(businessTrip.getWriter())
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

    BusinessTrip businessTrip = businessTripRepository.findById(businessTripId);
    businessTrip.update(request);

    return BusinessTripUpdateResponse.builder()
        .businessTripId(businessTripId)
        .build();
  }

  @Transactional(readOnly = true)
  public BusinessTripGetAllResponse getAllBusinessTrip() {
    List<BusinessTrip> businessTripList = businessTripRepository.findAll();
    return BusinessTripGetAllResponse.builder()
        .businessTripList(businessTripList)
        .build();
  }

}