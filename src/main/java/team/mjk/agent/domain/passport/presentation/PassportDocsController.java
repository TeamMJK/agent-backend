package team.mjk.agent.domain.passport.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import team.mjk.agent.domain.passport.application.dto.response.PassportInfoResponse;
import team.mjk.agent.domain.passport.application.dto.response.PassportSaveResponse;
import team.mjk.agent.domain.passport.application.dto.response.PassportUpdateResponse;
import team.mjk.agent.domain.passport.presentation.request.PassportSaveRequest;
import team.mjk.agent.domain.passport.presentation.request.PassportUpdateRequest;
import team.mjk.agent.global.annotation.MemberId;

@Tag(name = "Passport", description = "여권 관련 API (저장, 조회, 수정)")
public interface PassportDocsController {

    @Operation(summary = "여권 정보 저장", description = "회원의 여권 정보를 저장합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "여권 정보 저장 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PassportSaveResponse.class)
                    )
            )
    })
    ResponseEntity<PassportSaveResponse> save(
            @Parameter(hidden = true) @MemberId Long memberId,
            @RequestBody(description = "여권 저장 요청 DTO", required = true) PassportSaveRequest request
    );

    @Operation(summary = "여권 정보 수정", description = "회원의 여권 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "여권 정보 수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PassportUpdateResponse.class)
                    )
            )
    })
    ResponseEntity<PassportUpdateResponse> update(
            @Parameter(hidden = true) @MemberId Long memberId,
            @RequestBody(description = "여권 수정 요청 DTO", required = true) PassportUpdateRequest request
    );

    @Operation(summary = "여권 정보 조회", description = "회원의 여권 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "여권 정보 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PassportInfoResponse.class)
                    )
            )
    })
    ResponseEntity<PassportInfoResponse> getPassportInfo(
            @Parameter(hidden = true) @MemberId Long memberId
    );

}
