package team.mjk.agent.domain.businessTrip.dto.request;

import java.time.LocalDate;
import java.util.List;

public record BusinessTripSaveRequest(
    LocalDate departDate,
    LocalDate arriveDate,
    String destination,
    List<String> names
) {
}