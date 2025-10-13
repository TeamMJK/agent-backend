package team.mjk.agent.domain.review.presentation.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import team.mjk.agent.domain.review.application.dto.request.ReviewSaveServiceRequest;

@Builder
public record ReviewSaveRequest(

        @NotBlank(message = "리뷰 내용을 입력해 주세요.")
        String reviewContent,

        @Min(value = 1, message = "별점은 1점 이상이어야 합니다")
        @Max(value = 5, message = "별점은 5점 이하만 가능합니다")
        int rating

) {

    public ReviewSaveServiceRequest toReviewSaveServiceRequest(Long memberId) {
        return ReviewSaveServiceRequest.builder()
                .memberId(memberId)
                .reviewContent(reviewContent)
                .rating(rating)
                .build();
    }

}
