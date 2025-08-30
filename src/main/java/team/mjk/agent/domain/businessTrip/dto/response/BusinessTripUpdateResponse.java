package team.mjk.agent.domain.businessTrip.dto.response;

import lombok.Builder;

@Builder
public record BusinessTripUpdateResponse(
    Long businessTripId
) {

}