package team.mjk.agent.domain.businessTrip.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record BusinessTripGetResponse(
    LocalDate departDate,
    LocalDate arriveDate,
    String destination,
    List<String> names,
    String writer
) {

}
