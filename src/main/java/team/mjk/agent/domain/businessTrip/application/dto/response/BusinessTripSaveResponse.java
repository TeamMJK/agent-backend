package team.mjk.agent.domain.businessTrip.application.dto.response;

import lombok.Builder;

@Builder
public record BusinessTripSaveResponse(
    Long businessTripId
) {

}