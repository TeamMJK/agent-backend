package team.mjk.agent.domain.review.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import team.mjk.agent.domain.review.application.dto.response.ReviewAverageResponse;
import team.mjk.agent.domain.review.application.dto.response.ReviewSaveResponse;
import team.mjk.agent.domain.review.presentation.request.ReviewSaveRequest;

@Tag(name = "Review", description = "리뷰 관련 API (리뷰 등록, 평균 평점 조회)")
public interface ReviewDocsController {

    @Operation(summary = "리뷰 등록", description = "새로운 리뷰를 저장합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "리뷰 등록 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewSaveResponse.class)
                    )
            )
    })
    ResponseEntity<ReviewSaveResponse> save(
            @RequestBody(
                    description = "리뷰 저장 요청 DTO",
                    required = true
            ) ReviewSaveRequest request
    );

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

}
