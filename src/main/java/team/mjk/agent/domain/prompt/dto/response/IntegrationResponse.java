package team.mjk.agent.domain.prompt.dto.response;

import team.mjk.agent.domain.member.dto.response.MemberInfoList;

public record IntegrationResponse(
    BusinessTripList businessTripList,
    HotelList hotelList,
    MemberInfoList memberInfoList
) {

}