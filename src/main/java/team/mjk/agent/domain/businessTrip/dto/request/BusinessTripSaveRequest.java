package team.mjk.agent.domain.businessTrip.dto.request;

import java.time.LocalDate;
import java.util.List;
import team.mjk.agent.domain.businessTrip.domain.ServiceType;

public record BusinessTripSaveRequest(
    LocalDate departDate,
    LocalDate arriveDate,
    String destination,
    List<String> names,
    ServiceType serviceType
) {
}