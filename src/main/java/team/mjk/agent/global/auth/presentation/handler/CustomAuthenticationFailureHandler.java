package team.mjk.agent.global.auth.presentation.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import team.mjk.agent.global.jwt.config.SecurityProperties;

import java.io.IOException;

import static team.mjk.agent.global.auth.presentation.exception.AuthExceptionCode.AUTHENTICATION_REQUIRED;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final SecurityProperties securityProperties;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException exception
    ) throws IOException, ServletException {
        super.setDefaultFailureUrl(securityProperties.loginUrl() + "?error=true&exception=" + AUTHENTICATION_REQUIRED);
        super.onAuthenticationFailure(request, response, exception);
    }
}
