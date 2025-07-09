package team.mjk.agent.global.auth.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Auth", description = "OAuth2 로그인 리다이렉트 API")
public interface OauthDocsController {

    @Operation(
            summary = "구글 로그인 리다이렉트",
            description = "Google OAuth2 인증을 위한 리다이렉트 URL로 이동합니다."
    )
    @GetMapping
    String login();

}
