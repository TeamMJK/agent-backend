package team.mjk.agent.domain.review.presentation.query;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.mjk.agent.domain.review.application.dto.response.ReviewAverageResponse;
import team.mjk.agent.domain.review.application.query.ReviewQueryService;
import team.mjk.agent.global.annotation.MemberId;

@RequiredArgsConstructor
@RequestMapping("/reviews")
@RestController
public class ReviewQueryController implements ReviewQueryDocsController {

    private final ReviewQueryService reviewQueryService;

    @GetMapping
    public ResponseEntity<ReviewAverageResponse> getAverageRating() {
        ReviewAverageResponse response = reviewQueryService.getAverageRating();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/written")
    public ResponseEntity<Boolean> checkReviewWritten(@MemberId Long memberId) {
        boolean hasWritten = reviewQueryService.hasWrittenReview(memberId);
        return ResponseEntity.ok(hasWritten);
    }

}
