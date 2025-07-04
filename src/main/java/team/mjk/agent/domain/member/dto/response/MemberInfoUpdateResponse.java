package team.mjk.agent.domain.member.dto.response;

import lombok.Builder;

@Builder
public record MemberInfoUpdateResponse(

    Long memberId

) {
}