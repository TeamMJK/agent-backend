package team.mjk.agent.domain.agoda.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import team.mjk.agent.domain.agoda.dto.response.AgodaHotelResponse;
import team.mjk.agent.domain.prompt.dto.request.PromptRequest;
import team.mjk.agent.global.annotation.MemberId;

import java.util.List;

@Tag(name = "Agoda", description = "Agoda 호텔 조회 API")
public interface AgodaDocsController {

    @Operation(summary = "호텔 검색", description = "회원의 요청에 따라 Agoda 호텔 리스트를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "호텔 검색 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AgodaHotelResponse.class)
                    )
            )
    })
    List<AgodaHotelResponse> searchHotels(
            @Parameter(hidden = true) @MemberId Long memberId,
            @RequestBody(description = "호텔 검색 요청 DTO", required = true)
            PromptRequest request
    );

}
