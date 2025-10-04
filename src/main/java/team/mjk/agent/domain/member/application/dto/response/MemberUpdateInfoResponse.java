package team.mjk.agent.domain.member.application.dto.response;

import lombok.Builder;

@Builder
public record MemberUpdateInfoResponse(

        Long memberId

) {
}
