package team.mjk.agent.domain.review.presentation.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import team.mjk.agent.domain.review.application.dto.request.ReviewSaveServiceRequest;

@Builder
public record ReviewSaveRequest(

        String reviewContent,

        @Min(value = 1, message = "별점은 1점 이상이어야 합니다")
        @Max(value = 5, message = "별점은 5점 이하만 가능합니다")
        int rating

) {

    public ReviewSaveServiceRequest toReviewSaveServiceRequest() {
        return ReviewSaveServiceRequest.builder()
                .reviewContent(reviewContent)
                .rating(rating)
                .build();
    }

}
