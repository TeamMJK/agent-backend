package team.mjk.agent.global.auth.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class AuthenticationRequiredException extends CustomException {
    public AuthenticationRequiredException() {
        super(AuthExceptionCode.AUTHENTICATION_REQUIRED);
    }
}
