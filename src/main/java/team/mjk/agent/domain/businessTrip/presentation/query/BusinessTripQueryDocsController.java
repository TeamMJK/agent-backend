package team.mjk.agent.domain.businessTrip.presentation.query;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import team.mjk.agent.domain.businessTrip.application.dto.response.BusinessTripGetAllResponse;
import team.mjk.agent.domain.businessTrip.application.dto.response.BusinessTripGetResponse;

@Tag(name = "BusinessTrip", description = "출장 관련 API")
@RequestMapping("/business-trips")
public interface BusinessTripQueryDocsController {
    @Operation(summary = "출장 상세 조회", description = "특정 출장 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "출장 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BusinessTripGetResponse.class))
            )
    })
    ResponseEntity<BusinessTripGetResponse> getBusinessTrip(
            @Parameter(hidden = true) Long memberId,
            @Parameter(description = "출장 ID", required = true) Long businessTripId
    );

    @Operation(summary = "출장 목록 조회", description = "회원의 모든 출장 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "출장 목록 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BusinessTripGetAllResponse.class))
            )
    })
    ResponseEntity<BusinessTripGetAllResponse> getAllBusinessTrip(
            @Parameter(hidden = true) Long memberId
    );

}
