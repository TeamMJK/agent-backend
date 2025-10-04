package team.mjk.agent.domain.member.application.dto.request;

import lombok.Builder;

@Builder
public record MemberSaveServiceRequest(

        String email,

        String password

) {
}
