package team.mjk.agent.domain.member.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import team.mjk.agent.domain.member.application.dto.response.MemberSaveInfoResponse;
import team.mjk.agent.domain.member.application.dto.response.MemberUpdateInfoResponse;
import team.mjk.agent.domain.member.application.dto.response.MemberInfoGetResponse;
import team.mjk.agent.domain.member.presentation.request.MemberSaveInfoRequest;
import team.mjk.agent.domain.member.presentation.request.MemberSaveRequest;
import team.mjk.agent.domain.member.application.dto.response.MemberSaveResponse;
import team.mjk.agent.domain.member.presentation.request.MemberUpdateInfoRequest;
import team.mjk.agent.global.annotation.MemberId;

@Tag(name = "Member", description = "회원 관련 API (회원가입, 민감 정보 저장/조회/수정)")
public interface MemberDocsController {

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "회원가입 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberSaveResponse.class)
                    )
            )
    })
    ResponseEntity<MemberSaveResponse> signUp(
            @RequestBody(description = "회원가입 요청 DTO", required = true)
            MemberSaveRequest request
    );

    @Operation(summary = "민감 회원 정보 저장", description = "회원의 민감한 정보를 저장합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "민감 정보 저장 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberSaveInfoResponse.class)
                    )
            )
    })
    ResponseEntity<MemberSaveInfoResponse> saveMemberInfo(
            @Parameter(hidden = true) @MemberId Long memberId,
            @RequestBody(description = "민감 정보 저장 요청 DTO", required = true)
            MemberSaveInfoRequest request
    );

    @Operation(summary = "내 민감 정보 조회", description = "회원의 민감한 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "민감 정보 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberInfoGetResponse.class)
                    )
            )
    })
    ResponseEntity<MemberInfoGetResponse> getMemberInfo(
            @Parameter(hidden = true) @MemberId Long memberId
    );

    @Operation(summary = "내 민감 정보 수정", description = "회원의 민감한 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "민감 정보 수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberUpdateInfoResponse.class)
                    )
            )
    })
    ResponseEntity<MemberUpdateInfoResponse> updateMemberInfo(
            @Parameter(hidden = true) @MemberId Long memberId,
            @RequestBody(description = "민감 정보 수정 요청 DTO", required = true)
            MemberUpdateInfoRequest request
    );

}
