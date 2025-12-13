package team.mjk.agent.domain.businessTrip.presentation.request;

import java.time.LocalDate;
import java.util.List;

import team.mjk.agent.domain.businessTrip.application.dto.request.BusinessTripSaveServiceRequest;
import team.mjk.agent.domain.businessTrip.domain.ServiceType;

public record BusinessTripSaveRequest(

        LocalDate departDate,

        LocalDate arriveDate,

        String destination,

        List<String> names,

        ServiceType serviceType

) {

    public BusinessTripSaveServiceRequest toServiceRequest(Long memberId) {
        return BusinessTripSaveServiceRequest.builder()
                .memberId(memberId)
                .departDate(departDate)
                .arriveDate(arriveDate)
                .destination(destination)
                .names(names)
                .serviceType(serviceType)
                .build();
    }

}