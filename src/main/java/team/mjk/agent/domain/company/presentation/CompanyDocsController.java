package team.mjk.agent.domain.company.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import team.mjk.agent.domain.company.dto.request.CompanyInvitationCodeRequest;
import team.mjk.agent.domain.company.dto.request.CompanySaveRequest;

@Tag(name = "Company", description = "회사 관련 API")
@RequestMapping("/companies")
public interface CompanyDocsController {

    @Operation(summary = "초대 코드 생성", description = "회사 초대 코드를 생성합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "초대 코드 생성 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            )
    })
    ResponseEntity<String> createInvitationCode(
            @Parameter(description = "회사 ID", required = true) Long companyId
    );

    @Operation(summary = "회사 생성", description = "새로운 회사를 생성합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "회사 생성 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))
            )
    })
    ResponseEntity<Long> createCompany(
            @Parameter(hidden = true) Long memberId,
            @RequestBody(description = "회사 생성 요청 DTO", required = true) CompanySaveRequest request
    );

    @Operation(summary = "회사 가입", description = "초대 코드를 이용해 회사에 가입합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "회사 가입 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            )
    })
    ResponseEntity<String> joinCompany(
            @Parameter(hidden = true) Long memberId,
            @RequestBody(description = "회사 초대 코드 요청 DTO", required = true) CompanyInvitationCodeRequest request
    );

    @Operation(summary = "회사 정보", description = "회사 정보에서 회사 이름을 가져옵니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "회사 정보 가져오기 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
        )
    })
    ResponseEntity<String> getCompany(
        @Parameter(hidden = true) Long memberId
    );

}
