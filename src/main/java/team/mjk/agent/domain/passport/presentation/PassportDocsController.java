package team.mjk.agent.domain.passport.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import team.mjk.agent.domain.passport.dto.request.PassportInfoSaveRequest;
import team.mjk.agent.domain.passport.dto.response.PassportInfoSaveResponse;

@Tag(name = "Passport", description = "여권 정보 API")
public interface PassportDocsController {

    @Operation(
            summary = "여권 정보 저장",
            description = "회원의 여권 정보를 저장합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "여권 정보 저장 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PassportInfoSaveResponse.class)
                    )
            )
    })
    ResponseEntity<PassportInfoSaveResponse> savePassportInfo(
            @io.swagger.v3.oas.annotations.Parameter(hidden = true) Long memberId,
            @RequestBody(description = "여권 정보 저장 요청 DTO", required = true)
            PassportInfoSaveRequest request
    );

}
