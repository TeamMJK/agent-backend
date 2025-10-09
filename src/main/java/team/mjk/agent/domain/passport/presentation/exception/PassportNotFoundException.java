package team.mjk.agent.domain.passport.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class PassportNotFoundException extends CustomException {
    public PassportNotFoundException() {
        super(PassportExceptionCode.PASSPORT_NOT_FOUND);
    }
}
