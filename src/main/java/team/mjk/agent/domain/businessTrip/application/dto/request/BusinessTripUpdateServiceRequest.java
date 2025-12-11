package team.mjk.agent.domain.businessTrip.application.dto.request;

import lombok.Builder;
import team.mjk.agent.domain.businessTrip.domain.ServiceType;

import java.time.LocalDate;
import java.util.List;

@Builder
public record BusinessTripUpdateServiceRequest(

        Long memberId,

        Long businessTripId,

        LocalDate departDate,

        LocalDate arriveDate,

        String destination,

        List<String> names,

        ServiceType serviceType

) {
}
