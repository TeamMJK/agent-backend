package team.mjk.agent.domain.company.dto.response;

import java.util.List;
import lombok.Builder;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.dto.response.MemberInfoGetResponse;

@Builder
public record CompanyMemberListResponse(
    List<MemberInfoGetResponse> members
) {

}