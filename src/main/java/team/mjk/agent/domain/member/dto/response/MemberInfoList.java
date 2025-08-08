package team.mjk.agent.domain.member.dto.response;

import java.util.List;

public record MemberInfoList(
    List<MemberInfoGetResponse> memberInfoGetResponseList
) {

}
