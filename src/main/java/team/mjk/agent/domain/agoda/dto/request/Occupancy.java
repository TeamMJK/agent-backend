package team.mjk.agent.domain.agoda.dto.request;

import lombok.Builder;

@Builder
public record Occupancy(

        int numberOfAdult,
        int numberOfChildren

) {
}
