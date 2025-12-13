package team.mjk.agent.domain.businessTrip.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import team.mjk.agent.domain.businessTrip.presentation.request.BusinessTripSaveRequest;
import team.mjk.agent.domain.businessTrip.presentation.request.BusinessTripUpdateRequest;
import team.mjk.agent.domain.businessTrip.application.dto.response.BusinessTripGetAllResponse;
import team.mjk.agent.domain.businessTrip.application.dto.response.BusinessTripGetResponse;
import team.mjk.agent.domain.businessTrip.application.dto.response.BusinessTripSaveResponse;
import team.mjk.agent.domain.businessTrip.application.dto.response.BusinessTripUpdateResponse;
import team.mjk.agent.domain.companyworkspace.domain.Workspace;

@Tag(name = "BusinessTrip", description = "출장 관련 API")
@RequestMapping("/business-trips")
public interface BusinessTripDocsController {

    @Operation(summary = "출장 등록", description = "사용자가 WorkSpace 상관없이 MJK 워크스페이스에 저장할 때 출장 신청 정보를 등록합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "출장 등록 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BusinessTripSaveResponse.class))
            )
    })
    ResponseEntity<BusinessTripSaveResponse> saveBusinessTrip(
            @Parameter(hidden = true) Long memberId,
            @RequestBody(description = "출장 등록 요청 DTO", required = true) BusinessTripSaveRequest request
    );

    @Operation(summary = "출장 MCP 등록", description = "사용자가 출장 정보를 생성할 때 출장 신청 정보를 회사 MCP 에 등록하니다.(MJK 워크스페이스 포함)")
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "출장 정보 MCP 에 등록 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BusinessTripSaveResponse.class))
        )
    })
    ResponseEntity<List<Workspace>> saveBusinessTripMcp(
        @Parameter(hidden = true) Long memberId,
        @RequestBody(description = "출장 등록 요청 DTO", required = true) BusinessTripSaveRequest request
    );

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

    @Operation(summary = "출장 수정", description = "출장 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "출장 수정 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BusinessTripUpdateResponse.class))
            )
    })
    ResponseEntity<BusinessTripUpdateResponse> updateBusinessTrip(
            @Parameter(hidden = true) Long memberId,
            @Parameter(description = "출장 ID", required = true) Long businessTripId,
            @RequestBody(description = "출장 수정 요청 DTO", required = true) BusinessTripUpdateRequest request
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