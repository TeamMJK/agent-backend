package team.mjk.agent.domain.company.dto.response;

import java.util.List;
import lombok.Builder;
import team.mjk.agent.domain.member.application.dto.response.MemberGetInfoResponse2;
import team.mjk.agent.domain.member.application.dto.response.MemberInfoGetResponse;

@Builder
public record CompanyMemberListResponse(
    List<MemberInfoGetResponse> members,

    List<MemberGetInfoResponse2> members2

) {

}