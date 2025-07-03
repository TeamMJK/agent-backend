package team.mjk.agent.domain.member.dto.response;

import lombok.Builder;

@Builder
public record MemberInfoSaveResponse(

        Long memberId

) {
}
