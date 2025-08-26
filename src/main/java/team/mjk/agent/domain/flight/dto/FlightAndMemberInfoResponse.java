package team.mjk.agent.domain.flight.dto;

import team.mjk.agent.domain.member.dto.response.MemberInfoList;

public record FlightAndMemberInfoResponse(
    FlightList flightList,
    MemberInfoList memberInfoList
) {

}