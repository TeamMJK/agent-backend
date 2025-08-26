package team.mjk.agent.domain.hotel.dto;

import team.mjk.agent.domain.member.dto.response.MemberInfoList;

public record HotelAndMemberInfoResponse(
    HotelList hotelList,
    MemberInfoList memberInfoList
) {

}