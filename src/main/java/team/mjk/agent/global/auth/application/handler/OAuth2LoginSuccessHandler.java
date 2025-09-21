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
            log.info("[OAuth2LoginSuccessHandler] Authentication Success: {}", authentication.getName());

            OauthLoginResultResponse result = resolveLoginResultFromAuthentication(authentication);
            log.info("[OAuth2LoginSuccessHandler] Login result: memberId={}, isFirstLogin={}",
                    result.memberId(), result.isFirstLogin());

            tokenInjector.injectTokensToCookie(result, response);
            log.info("[OAuth2LoginSuccessHandler] Tokens injected to cookie");

            redirectToSuccessUrl(request, response, result);

        } catch (AlreadyRegisteredMemberException e) {
            log.warn("[OAuth2LoginSuccessHandler] AlreadyRegisteredMemberException 발생! Redirecting to login URL", e);
            handleAlreadyExistUser(response);
        } catch (Exception e) {
            log.error("[OAuth2LoginSuccessHandler] 알 수 없는 예외 발생! Redirecting to login URL", e);
            response.sendRedirect(securityProperties.loginUrl() + "?error=true&exception=UNKNOWN_ERROR");
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
        String redirectUrl = determineRedirectUrl(redirectUrlByCookie, result.isFirstLogin());

        log.info("[OAuth2LoginSuccessHandler] Redirecting to URL (before sendRedirect): {}", redirectUrl);

        response.sendRedirect(redirectUrl);
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
        String targetUrl = StringUtils.hasText(redirectCookie)
                ? redirectCookie
                : (isFirstLogin ? securityProperties.firstLoginUrl() : securityProperties.defaultUrl());

        log.info("[OAuth2LoginSuccessHandler] Redirecting to: {}", targetUrl);
        return targetUrl;
    }

    private void handleAlreadyExistUser(HttpServletResponse response) throws IOException {
        response.sendRedirect(securityProperties.loginUrl() + "?error=true&exception=" + ALREADY_REGISTERED_MEMBER);
    }

}
