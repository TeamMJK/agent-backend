package team.mjk.agent.global.jwt.injector;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import team.mjk.agent.global.auth.dto.response.TokenResult;
import team.mjk.agent.global.jwt.config.SecurityProperties;
import team.mjk.agent.global.jwt.config.TokenProperties;

import static team.mjk.agent.global.jwt.resolver.JwtTokenResolver.ACCESS_TOKEN;
import static team.mjk.agent.global.jwt.resolver.JwtTokenResolver.REFRESH_TOKEN;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenInjector {

    private final TokenProperties tokenProperties;
    private final SecurityProperties securityProperties;

    public void injectTokensToCookie(TokenResult result, HttpServletResponse response) {
        int accessTokenMaxAge = (int)tokenProperties.expirationTime().accessToken() + 5;
        int refreshTokenMaxAge = (int)tokenProperties.expirationTime().refreshToken() + 5;

        addCookie(ACCESS_TOKEN, result.accessToken(), accessTokenMaxAge, response);
        addCookie(REFRESH_TOKEN, result.refreshToken(), refreshTokenMaxAge, response);
    }

    public void addCookie(String name, String value, int maxAge, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(securityProperties.cookie().httpOnly());
        cookie.setDomain(securityProperties.cookie().domain());
        cookie.setSecure(securityProperties.cookie().secure());
        cookie.setAttribute("SameSite", "Lax");

        response.addCookie(cookie);
    }

    public void invalidateCookie(String name, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(securityProperties.cookie().httpOnly());
        cookie.setDomain(securityProperties.cookie().domain());
        cookie.setSecure(securityProperties.cookie().secure());
        cookie.setAttribute("SameSite", "Lax");

        response.addCookie(cookie);
    }
}
