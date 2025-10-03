package team.mjk.agent.domain.agoda.dto.request;

import lombok.Builder;
import team.mjk.agent.domain.hotel.dto.AgodaHotel;

@Builder
public record Criteria(

    Additional additional,
    String checkInDate,
    String checkOutDate,
    int cityId

) {

}