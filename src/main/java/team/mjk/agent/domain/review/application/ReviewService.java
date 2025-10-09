package team.mjk.agent.domain.review.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.review.application.dto.request.ReviewSaveServiceRequest;
import team.mjk.agent.domain.review.application.dto.response.ReviewAverageResponse;
import team.mjk.agent.domain.review.application.dto.response.ReviewSaveResponse;
import team.mjk.agent.domain.review.domain.Review;
import team.mjk.agent.domain.review.domain.ReviewRepository;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewSaveResponse saveReview(ReviewSaveServiceRequest request) {
        Review review = Review.builder()
                .reviewContent(request.reviewContent())
                .rating(request.rating())
                .build();
        reviewRepository.save(review);

        return ReviewSaveResponse.builder()
                .reviewId(review.getId())
                .build();
    }

    @Transactional(readOnly = true)
    public ReviewAverageResponse calculateAverageRating() {
        Double avg = reviewRepository.findAverageRating();
        int rounded = 0;

        if (avg != null) {
            rounded = (int) Math.round(avg);
        }

        return ReviewAverageResponse.builder()
                .averageRating(rounded)
                .build();
    }

}
