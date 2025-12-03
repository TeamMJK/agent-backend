package team.mjk.agent.domain.company.application.dto.response;

import java.util.List;
import lombok.Builder;
import team.mjk.agent.domain.member.application.dto.response.MemberInfoGetResponse;

@Builder
public record CompanyMemberListResponse(
    List<MemberInfoGetResponse> members
) {

}