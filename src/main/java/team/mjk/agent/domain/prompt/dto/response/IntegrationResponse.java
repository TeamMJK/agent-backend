package team.mjk.agent.domain.prompt.dto.response;

import team.mjk.agent.domain.flight.dto.FlightList;
import team.mjk.agent.domain.hotel.dto.HotelList;
import team.mjk.agent.domain.member.dto.response.MemberInfoList;

public record IntegrationResponse(
    FlightList flightList,
    HotelList hotelList,
    MemberInfoList memberInfoList
) {

}