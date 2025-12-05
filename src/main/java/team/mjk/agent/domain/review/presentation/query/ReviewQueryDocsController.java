package team.mjk.agent.domain.review.presentation.query;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import team.mjk.agent.domain.review.application.dto.response.ReviewAverageResponse;

@Tag(name = "Review", description = "리뷰 관련 API (리뷰 등록, 평균 평점 조회, 작성 여부 확인)")
public interface ReviewQueryDocsController {

    @Operation(summary = "평균 평점 조회", description = "등록된 리뷰들의 평균 평점을 계산하여 반환합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "평균 평점 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewAverageResponse.class)
                    )
            )
    })
    ResponseEntity<ReviewAverageResponse> getAverageRating();

    @Operation(summary = "리뷰 작성 여부 확인", description = "회원이 리뷰를 작성했는지 여부를 반환합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "리뷰가 존재할 경우 true 반환",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "리뷰가 존재하지 않을 경우 ReviewNotFoundException 발생",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<Boolean> checkReviewWritten(
            @Schema(hidden = true) Long memberId
    );

}
