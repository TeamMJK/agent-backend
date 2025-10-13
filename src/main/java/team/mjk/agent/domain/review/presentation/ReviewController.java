package team.mjk.agent.domain.review.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.mjk.agent.domain.review.application.ReviewService;
import team.mjk.agent.domain.review.application.dto.response.ReviewAverageResponse;
import team.mjk.agent.domain.review.application.dto.response.ReviewSaveResponse;
import team.mjk.agent.domain.review.presentation.request.ReviewSaveRequest;
import team.mjk.agent.global.annotation.MemberId;

@RequiredArgsConstructor
@RequestMapping("/reviews")
@RestController
public class ReviewController implements ReviewDocsController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewSaveResponse> save(
            @MemberId Long memberId,
            @Valid @RequestBody ReviewSaveRequest request
    ) {
        ReviewSaveResponse response = reviewService.saveReview(request.toReviewSaveServiceRequest(memberId));
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<ReviewAverageResponse> getAverageRating() {
        ReviewAverageResponse response = reviewService.calculateAverageRating();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/written")
    public ResponseEntity<Boolean> checkReviewWritten(@MemberId Long memberId) {
        boolean hasWritten = reviewService.hasWrittenReview(memberId);
        return ResponseEntity.ok(hasWritten);
    }

}
