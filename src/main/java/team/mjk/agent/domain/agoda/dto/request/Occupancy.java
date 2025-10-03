package team.mjk.agent.domain.agoda.dto.request;

import java.util.List;
import java.util.Optional;
import lombok.Builder;

@Builder
public record Occupancy(
    Integer numberOfAdult,
    Integer numberOfChildren,
    List<Integer> childrenAges
) {

}