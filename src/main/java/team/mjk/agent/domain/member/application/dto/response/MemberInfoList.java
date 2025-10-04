package team.mjk.agent.domain.member.application.dto.response;

import java.util.List;

public record MemberInfoList(
    List<MemberInfoGetResponse> memberInfoGetResponseList
) {

}
