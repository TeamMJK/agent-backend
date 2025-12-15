package team.mjk.agent.domain.businessTrip.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.businessTrip.application.dto.request.BusinessTripSaveServiceRequest;
import team.mjk.agent.domain.businessTrip.application.dto.response.BusinessTripSaveResponse;
import team.mjk.agent.domain.businessTrip.domain.BusinessTrip;
import team.mjk.agent.domain.businessTrip.domain.BusinessTripRepository;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;

@RequiredArgsConstructor
@Service
public class BusinessTripCommandService {

    private final BusinessTripRepository businessTripRepository;
    private final MemberRepository memberRepository;

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

}
