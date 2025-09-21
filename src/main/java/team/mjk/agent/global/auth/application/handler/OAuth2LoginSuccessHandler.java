package team.mjk.agent.global.auth.application.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import team.mjk.agent.global.auth.application.AuthService;
import team.mjk.agent.global.auth.application.OAuthLoginService;
import team.mjk.agent.global.auth.domain.CustomOAuth2User;
import team.mjk.agent.global.auth.dto.response.OauthLoginResultResponse;
import team.mjk.agent.global.auth.presentation.exception.AlreadyRegisteredMemberException;
import team.mjk.agent.global.jwt.config.SecurityProperties;
import team.mjk.agent.global.jwt.injector.TokenInjector;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static team.mjk.agent.global.auth.presentation.exception.AuthExceptionCode.ALREADY_REGISTERED_MEMBER;
import static team.mjk.agent.global.auth.presentation.filter.RedirectUrlFilter.REDIRECT_URL_COOKIE_NAME;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final OAuthLoginService oAuth2LoginService;
    private final TokenInjector tokenInjector;
    private final SecurityProperties securityProperties;

//    @Override
//    public void onAuthenticationSuccess(
//            HttpServletRequest request, HttpServletResponse response, Authentication authentication
//    ) throws IOException {
//        try {
//            OauthLoginResultResponse result = resolveLoginResultFromAuthentication(authentication);
//            tokenInjector.injectTokensToCookie(result, response);
//            redirectToSuccessUrl(request, response, result);
//        } catch (AlreadyRegisteredMemberException e) {
//            handleAlreadyExistUser(response);
//        }
//    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication
    ) throws IOException {
        try {
            OauthLoginResultResponse result = resolveLoginResultFromAuthentication(authentication);

            tokenInjector.injectTokensToCookie(result, response);
            log.info("[OAuth2LoginSuccessHandler] Login result: memberId={}, isFirstLogin={}",
                    result.memberId(), result.isFirstLogin());
            log.info("[OAuth2LoginSuccessHandler] Tokens injected to cookie");

            redirectToSuccessUrl(request, response, result);

        } catch (AlreadyRegisteredMemberException e) {
            log.warn("[OAuth2LoginSuccessHandler] Already registered member: {}", e.getMessage());
            handleAlreadyExistUser(response);
        } catch (Exception e) {
            log.error("[OAuth2LoginSuccessHandler] Unexpected error during OAuth2 login", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "OAuth2 login failed");
        }
    }

    private OauthLoginResultResponse resolveLoginResultFromAuthentication(Authentication authentication) {
        CustomOAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();
        return oAuth2LoginService.handleLoginSuccess(oAuth2User.getAuthAttributes());
    }

//    private void redirectToSuccessUrl(
//            HttpServletRequest request, HttpServletResponse response, OauthLoginResultResponse result
//    ) throws IOException {
//        String redirectUrlByCookie = getRedirectUrlByCookie(request);
//        String redirectUrl = determineRedirectUrl(redirectUrlByCookie, result.isFirstLogin());
//        response.sendRedirect(redirectUrl);
//        tokenInjector.invalidateCookie(REDIRECT_URL_COOKIE_NAME, response);
//    }

    private void redirectToSuccessUrl(
            HttpServletRequest request, HttpServletResponse response, OauthLoginResultResponse result
    ) throws IOException {
        String redirectUrlByCookie = getRedirectUrlByCookie(request);
        log.info("[OAuth2LoginSuccessHandler] Redirect URL from cookie: {}", redirectUrlByCookie);

        String baseRedirectUrl = StringUtils.hasText(redirectUrlByCookie)
                ? redirectUrlByCookie
                : (result.isFirstLogin() ? securityProperties.firstLoginUrl() : securityProperties.defaultUrl());

        // 쿼리 파라미터로 토큰과 로그인 상태 전달
        String redirectUrl = String.format("%s?success=true&token=%s&refreshToken=%s&memberId=%d&firstLogin=%b",
                baseRedirectUrl,
                result.accessToken(),
                result.refreshToken(),
                result.memberId(),
                result.isFirstLogin());

        log.info("[OAuth2LoginSuccessHandler] Redirecting to: {}", redirectUrl);

        response.sendRedirect(redirectUrl);

        // 쿠키 무효화는 유지
        tokenInjector.invalidateCookie(REDIRECT_URL_COOKIE_NAME, response);
    }

    private String getRedirectUrlByCookie(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> Objects.equals(cookie.getName(), REDIRECT_URL_COOKIE_NAME))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    private String determineRedirectUrl(String redirectCookie, boolean isFirstLogin) {
        return StringUtils.hasText(redirectCookie)
                ? redirectCookie
                : (isFirstLogin ? securityProperties.firstLoginUrl() : securityProperties.defaultUrl());
    }

//    private void handleAlreadyExistUser(HttpServletResponse response) throws IOException {
//        response.sendRedirect(securityProperties.loginUrl() + "?error=true&exception=" + ALREADY_REGISTERED_MEMBER);
//    }

    private void handleAlreadyExistUser(HttpServletResponse response) throws IOException {
        String errorRedirect = securityProperties.loginUrl() + "?error=true&exception=" + ALREADY_REGISTERED_MEMBER;
        log.info("[OAuth2LoginSuccessHandler] Redirecting already registered user to: {}", errorRedirect);
        response.sendRedirect(errorRedirect);
    }

}
