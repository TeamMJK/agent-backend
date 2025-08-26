package team.mjk.agent.domain.flight.dto;

import java.util.List;
import team.mjk.agent.domain.flight.dto.Flight;

public record FlightList(
    List<Flight> flights
) {

}