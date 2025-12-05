package team.mjk.agent.domain.review.presentation.command;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.mjk.agent.domain.review.application.command.ReviewCommandService;
import team.mjk.agent.domain.review.application.dto.response.ReviewSaveResponse;
import team.mjk.agent.domain.review.presentation.request.ReviewSaveRequest;
import team.mjk.agent.global.annotation.MemberId;

@RequiredArgsConstructor
@RequestMapping("/reviews")
@RestController
public class ReviewCommandController implements ReviewCommandDocsController {

    private final ReviewCommandService reviewCommandService;

    @PostMapping
    public ResponseEntity<ReviewSaveResponse> save(
            @MemberId Long memberId,
            @Valid @RequestBody ReviewSaveRequest request
    ) {
        ReviewSaveResponse response = reviewCommandService.saveReview(request.toReviewSaveServiceRequest(memberId));
        return ResponseEntity.status(201).body(response);
    }

}
