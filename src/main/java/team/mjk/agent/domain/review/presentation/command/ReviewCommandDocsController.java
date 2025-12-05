package team.mjk.agent.domain.review.presentation.command;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import team.mjk.agent.domain.review.application.dto.response.ReviewSaveResponse;
import team.mjk.agent.domain.review.presentation.request.ReviewSaveRequest;

@Tag(name = "Review", description = "리뷰 관련 API (리뷰 등록, 평균 평점 조회, 작성 여부 확인)")
public interface ReviewCommandDocsController {

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
            @Schema(hidden = true) Long memberId,
            @RequestBody(
                    description = "리뷰 저장 요청 DTO",
                    required = true
            ) ReviewSaveRequest request
    );

}
