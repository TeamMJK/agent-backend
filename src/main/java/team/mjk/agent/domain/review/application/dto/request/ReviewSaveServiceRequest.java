package team.mjk.agent.domain.review.application.dto.request;

import lombok.Builder;

@Builder
public record ReviewSaveServiceRequest(

        String reviewContent,

        int rating

) {
}
