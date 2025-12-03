package team.mjk.agent.domain.company.presentation.command;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import team.mjk.agent.domain.company.application.dto.response.CompanyInvitationEmailResponse;
import team.mjk.agent.domain.company.application.dto.response.CompanyJoinResponse;
import team.mjk.agent.domain.company.application.dto.response.CompanySaveResponse;
import team.mjk.agent.domain.company.presentation.request.CompanyInvitationCodeRequest;
import team.mjk.agent.domain.company.presentation.request.CompanyInvitationEmailRequest;
import team.mjk.agent.domain.company.presentation.request.CompanySaveRequest;

@Tag(name = "Company", description = "회사 관련 API")
public interface CompanyCommandDocsController {

    @Operation(summary = "회사 생성", description = "새로운 회사를 생성합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "회사 생성 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))
            )
    })
    ResponseEntity<CompanySaveResponse> createCompany(
            @Parameter(hidden = true) Long memberId,
            @RequestBody(description = "회사 생성 요청 DTO", required = true) CompanySaveRequest request
    );

    @Operation(summary = "초대 코드 생성 및 이메일 발송", description = "이메일로 회사 초대 코드를 발송합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "초대 코드 생성 및 발송 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CompanyInvitationEmailResponse.class)
                    )
            )
    })
    ResponseEntity<CompanyInvitationEmailResponse> sendInvitationCode(
            @Parameter(hidden = true) Long memberID,
            @RequestBody(description = "초대 코드 발송 요청 DTO", required = true)
            CompanyInvitationEmailRequest request
    );

    @Operation(summary = "회사 가입", description = "초대 코드를 이용해 회사에 가입합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "회사 가입 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            )
    })
    ResponseEntity<CompanyJoinResponse> joinCompany(
            @Parameter(hidden = true) Long memberId,
            @RequestBody(description = "회사 초대 코드 요청 DTO", required = true) CompanyInvitationCodeRequest request
    );

}
