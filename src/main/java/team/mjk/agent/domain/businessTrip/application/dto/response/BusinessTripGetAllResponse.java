package team.mjk.agent.domain.businessTrip.application.dto.response;

import java.util.List;
import lombok.Builder;
import team.mjk.agent.domain.businessTrip.domain.BusinessTrip;

@Builder
public record BusinessTripGetAllResponse(
    List<BusinessTrip> businessTripList
) {

}
