package team.mjk.agent.domain.review.application.dto.response;

import lombok.Builder;
import team.mjk.agent.domain.review.domain.Review;

@Builder
public record ReviewAverageResponse(

        int averageRating

) {

    public static ReviewAverageResponse from(int rounded) {
        return ReviewAverageResponse.builder()
                .averageRating(rounded)
                .build();
    }

}
