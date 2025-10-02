package team.mjk.agent.domain.agoda.dto.request;

import lombok.Builder;

@Builder
public record Additional(

        String currency,
        DailyRate dailyRate,
        boolean discountOnly,
        String language,
        int maxResult,
        int minimumReviewScore,
        int minimumStarRating,
        Occupancy occupancy,
        String sortBy

) {
}
