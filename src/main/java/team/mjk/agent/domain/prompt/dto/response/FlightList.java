package team.mjk.agent.domain.prompt.dto.response;

import java.util.List;

public record FlightList(
    List<Flight> flights
) {

}