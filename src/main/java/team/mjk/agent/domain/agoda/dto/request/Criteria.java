package team.mjk.agent.domain.agoda.dto.request;

import lombok.Builder;

@Builder
public record Criteria(

        Additional additional,
        String checkInDate,
        String checkOutDate,
        int cityId

) {
}
