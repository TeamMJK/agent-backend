package team.mjk.agent.domain.prompt.dto.response;

import team.mjk.agent.domain.member.dto.response.MemberInfoList;

public record HotelAndMemberInfoResponse(
    HotelList hotelList,
    MemberInfoList memberInfoList
) {

}