package team.mjk.agent.global.auth.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class RefreshTokenNotValidException extends CustomException {
    public RefreshTokenNotValidException() {
        super(AuthExceptionCode.REFRESH_TOKEN_NOT_VALID);
    }
}
