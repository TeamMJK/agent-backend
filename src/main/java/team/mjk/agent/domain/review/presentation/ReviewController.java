package team.mjk.agent.domain.review.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.mjk.agent.domain.review.application.ReviewService;
import team.mjk.agent.domain.review.application.dto.response.ReviewAverageResponse;
import team.mjk.agent.domain.review.application.dto.response.ReviewSaveResponse;
import team.mjk.agent.domain.review.presentation.request.ReviewSaveRequest;

@RequiredArgsConstructor
@RequestMapping("/reviews")
@RestController
public class ReviewController implements ReviewDocsController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewSaveResponse> save(
            @Valid @RequestBody ReviewSaveRequest request
    ) {
        ReviewSaveResponse response = reviewService.saveReview(request.toReviewSaveServiceRequest());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<ReviewAverageResponse> getAverageRating() {
        ReviewAverageResponse response = reviewService.calculateAverageRating();
        return ResponseEntity.ok(response);
    }

}
