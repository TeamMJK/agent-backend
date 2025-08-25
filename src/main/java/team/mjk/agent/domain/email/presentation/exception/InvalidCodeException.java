package team.mjk.agent.domain.email.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class InvalidCodeException extends CustomException {

    public InvalidCodeException() {
        super(EmailExceptionCode.INVALID_CODE);
    }

}
