package team.mjk.agent.global.jwt.filter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import team.mjk.agent.global.auth.application.RefreshTokenService;
import team.mjk.agent.global.auth.presentation.exception.AuthenticationRequiredException;
import team.mjk.agent.global.auth.presentation.exception.RefreshTokenNotValidException;
import team.mjk.agent.global.jwt.injector.TokenInjector;
import team.mjk.agent.global.jwt.resolver.JwtTokenResolver;

import java.io.IOException;
import java.util.Arrays;

import static team.mjk.agent.global.jwt.resolver.JwtTokenResolver.ACCESS_TOKEN;
import static team.mjk.agent.global.jwt.resolver.JwtTokenResolver.REFRESH_TOKEN;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenResolver jwtTokenResolver;
    private final TokenInjector tokenInjector;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;

    //    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
//    ) throws ServletException, IOException {
//        processTokenAuthentication(request, response);
//        filterChain.doFilter(request, response);
//    }
    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                log.info("[JwtTokenFilter] Incoming cookie - Name: {}, Value: {}",
                        cookie.getName(), cookie.getValue());
            }
        } else {
            log.info("[JwtTokenFilter] No cookies found in the request");
        }

        processTokenAuthentication(request, response);
        filterChain.doFilter(request, response);
    }

    //    private void processTokenAuthentication(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            String token = resolveTokenFromRequest(request, response);
//            setAuthentication(request, getUserDetails(token, request, response));
//        } catch (ExpiredJwtException | AuthenticationRequiredException e) {
//            log.debug("Failed to authenticate", e);
//            invalidateCookie(ACCESS_TOKEN, response);
//        } catch (RefreshTokenNotValidException e) {
//            log.warn("Failed to authenticate", e);
//            invalidateCookie(ACCESS_TOKEN, response);
//            invalidateCookie(REFRESH_TOKEN, response);
//        } catch (Exception e) {
//            log.error("Failed to authenticate", e);
//            invalidateCookie(ACCESS_TOKEN, response);
//        }
//    }
    private void processTokenAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            String token = resolveTokenFromRequest(request, response);

            // 토큰 값 로그
            log.info("[JwtTokenFilter] Resolved token: {}", token);

            UserDetails userDetails = getUserDetails(token, request, response);

            // 유저 정보 로그
            log.info("[JwtTokenFilter] UserDetails loaded - username: {}, authorities: {}",
                    userDetails.getUsername(), userDetails.getAuthorities());

            setAuthentication(request, userDetails);
        } catch (ExpiredJwtException e) {
            log.warn("[JwtTokenFilter] Token expired", e);
            invalidateCookie(ACCESS_TOKEN, response);
        } catch (AuthenticationRequiredException e) {
            log.warn("[JwtTokenFilter] Authentication required", e);
            invalidateCookie(ACCESS_TOKEN, response);
        } catch (RefreshTokenNotValidException e) {
            log.warn("[JwtTokenFilter] Refresh token invalid", e);
            invalidateCookie(ACCESS_TOKEN, response);
            invalidateCookie(REFRESH_TOKEN, response);
        } catch (Exception e) {
            log.error("[JwtTokenFilter] Unexpected error during authentication", e);
            invalidateCookie(ACCESS_TOKEN, response);
        }
    }

    //    private String resolveTokenFromRequest(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            return jwtTokenResolver.resolveTokenFromRequest(request)
//                    .orElseGet(() -> refreshTokenService.reissueBasedOnRefreshToken(request, response).accessToken());
//        } catch (ExpiredJwtException e) {
//            return refreshTokenService.reissueBasedOnRefreshToken(request, response).accessToken();
//        }
//    }
    private String resolveTokenFromRequest(HttpServletRequest request, HttpServletResponse response) {
        log.info("[JwtTokenFilter] Resolving token from request");
        if (request.getCookies() != null) {
            Arrays.stream(request.getCookies()).forEach(cookie ->
                    log.info("[JwtTokenFilter] Cookie - Name: {}, Value: {}", cookie.getName(), cookie.getValue())
            );
        } else {
            log.warn("[JwtTokenFilter] No cookies in request");
        }

        try {
            return jwtTokenResolver.resolveTokenFromRequest(request)
                    .orElseGet(() -> {
                        log.info("[JwtTokenFilter] Access token not found, trying refresh token");
                        return refreshTokenService.reissueBasedOnRefreshToken(request, response).accessToken();
                    });
        } catch (ExpiredJwtException e) {
            log.warn("[JwtTokenFilter] Access token expired, reissuing from refresh token");
            return refreshTokenService.reissueBasedOnRefreshToken(request, response).accessToken();
        } catch (Exception e) {
            log.error("[JwtTokenFilter] Failed to resolve token", e);
            return null;
        }
    }


    //    private UserDetails getUserDetails(String token, HttpServletRequest request, HttpServletResponse response) {
//        try {
//            String subject = jwtTokenResolver.getSubjectFromToken(token);
//            return userDetailsService.loadUserByUsername(subject);
//        } catch (ExpiredJwtException e) {
//            String accessToken = refreshTokenService.reissueBasedOnRefreshToken(request, response).accessToken();
//            String subject = jwtTokenResolver.getSubjectFromToken(accessToken);
//            return userDetailsService.loadUserByUsername(subject);
//        }
//    }
    private UserDetails getUserDetails(String token, HttpServletRequest request, HttpServletResponse response) {
        try {
            String subject = jwtTokenResolver.getSubjectFromToken(token);
            log.info("[JwtTokenFilter] Token subject: {}", subject); // 토큰에서 추출한 사용자 아이디
            return userDetailsService.loadUserByUsername(subject);
        } catch (ExpiredJwtException e) {
            log.warn("[JwtTokenFilter] Access token expired, trying refresh token", e);
            String accessToken = refreshTokenService.reissueBasedOnRefreshToken(request, response).accessToken();
            log.info("[JwtTokenFilter] New access token: {}", accessToken);
            String subject = jwtTokenResolver.getSubjectFromToken(accessToken);
            return userDetailsService.loadUserByUsername(subject);
        }
    }

    //    private void setAuthentication(HttpServletRequest request, UserDetails userDetails) {
//        UsernamePasswordAuthenticationToken authentication =
//                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }
    private void setAuthentication(HttpServletRequest request, UserDetails userDetails) {
        if (userDetails == null) {
            log.warn("[JwtTokenFilter] UserDetails is null, authentication will not be set");
            return;
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("[JwtTokenFilter] Authentication set for user: {}", userDetails.getUsername());
    }


    private void invalidateCookie(String cookieName, HttpServletResponse response) {
        tokenInjector.invalidateCookie(cookieName, response);
        SecurityContextHolder.clearContext();
    }

}
