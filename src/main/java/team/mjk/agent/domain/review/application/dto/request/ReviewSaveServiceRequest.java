package team.mjk.agent.domain.review.application.dto.request;

import lombok.Builder;

@Builder
public record ReviewSaveServiceRequest(

        Long memberId,

        String reviewContent,

        int rating

) {
}
