package team.mjk.agent.domain.hotel.dto;

import java.util.List;
import team.mjk.agent.domain.hotel.dto.Hotel;

public record HotelList(
    List<Hotel> hotels
) {

}
