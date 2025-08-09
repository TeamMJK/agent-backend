package team.mjk.agent.domain.prompt.dto.response;

import java.util.List;

public record Hotel(
    String departure_date,
    String arrival_date,
    String destination,
    int guests,
    int budget,
    List<String> requirements
) {

}