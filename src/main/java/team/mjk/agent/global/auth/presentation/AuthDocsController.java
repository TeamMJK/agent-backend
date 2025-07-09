package team.mjk.agent.global.auth.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import team.mjk.agent.global.auth.dto.request.LoginResultRequest;
import team.mjk.agent.global.auth.dto.response.LoginResultResponse;

@Tag(name = "Auth", description = "로그인 인증 API")
public interface AuthDocsController {

    @Operation(
            summary = "로그인 요청",
            description = "이메일과 비밀번호로 로그인을 시도하고, 로그인 결과를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResultResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패 (잘못된 이메일/비밀번호)"
            )
    })
    ResponseEntity<LoginResultResponse> login(
            @RequestBody(description = "로그인 요청 DTO", required = true)
            LoginResultRequest request,
            HttpServletResponse response
    );

}
