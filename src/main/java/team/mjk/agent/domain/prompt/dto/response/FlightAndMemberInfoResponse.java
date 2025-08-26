package team.mjk.agent.domain.prompt.dto.response;

import team.mjk.agent.domain.member.dto.response.MemberInfoList;

public record FlightAndMemberInfoResponse(
    FlightList flightList,
    MemberInfoList memberInfoList
) {

}