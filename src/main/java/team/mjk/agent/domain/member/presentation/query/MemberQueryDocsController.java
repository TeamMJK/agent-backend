package team.mjk.agent.domain.member.presentation.query;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import team.mjk.agent.domain.member.application.dto.response.MemberInfoGetResponse;
import team.mjk.agent.global.annotation.MemberId;

@Tag(name = "Member", description = "회원 관련 API (회원가입, 민감 정보 저장/조회/수정)")
public interface MemberQueryDocsController {

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

}
