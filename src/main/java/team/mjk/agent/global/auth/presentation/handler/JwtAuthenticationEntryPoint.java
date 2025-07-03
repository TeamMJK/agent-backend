package team.mjk.agent.global.auth.presentation.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import team.mjk.agent.global.exception.ExceptionResponse;

import java.io.IOException;

import static com.nimbusds.oauth2.sdk.http.HTTPResponse.SC_UNAUTHORIZED;
import static team.mjk.agent.global.auth.presentation.exception.AuthExceptionCode.AUTHENTICATION_REQUIRED;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException exception
    ) throws IOException {
        setResponseBodyBasicInfo(response);
        objectMapper.writeValue(response.getOutputStream(), ExceptionResponse.from(AUTHENTICATION_REQUIRED));
    }

    private void setResponseBodyBasicInfo(HttpServletResponse response) {
        response.setStatus(SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
    }
}
