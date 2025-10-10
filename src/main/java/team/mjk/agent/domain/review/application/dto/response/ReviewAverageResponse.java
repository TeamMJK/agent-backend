package team.mjk.agent.domain.review.application.dto.response;

import lombok.Builder;

@Builder
public record ReviewAverageResponse(

        int averageRating

) {
}
