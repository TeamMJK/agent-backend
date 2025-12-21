package team.mjk.agent.domain.businessTrip.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.businessTrip.application.dto.response.BusinessTripGetResponse;
import team.mjk.agent.domain.businessTrip.domain.BusinessTrip;
import team.mjk.agent.domain.businessTrip.domain.BusinessTripRepository;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;

@RequiredArgsConstructor
@Service
public class BusinessTripQueryService {

    private final BusinessTripRepository businessTripRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public BusinessTripGetResponse getBusinessTrip(Long memberId, Long businessTripId) {
        Member member = memberRepository.findByMemberId(memberId);
        Company company = member.getValidatedCompany();

        BusinessTrip businessTrip = businessTripRepository.findByIdAndCompanyId(
                businessTripId,
                company.getId()
        );

        return BusinessTripGetResponse.builder()
                .departDate(businessTrip.getDepartDate())
                .arriveDate(businessTrip.getArriveDate())
                .destination(businessTrip.getDestination())
                .names(businessTrip.getNames())
                .writer(businessTrip.getWriter())
                .serviceType(businessTrip.getServiceType())
                .build();
    }

}
