package team.mjk.agent.global.auth.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class AlreadyRegisteredMemberException extends CustomException {
    public AlreadyRegisteredMemberException() {
        super(AuthExceptionCode.ALREADY_REGISTERED_MEMBER);
    }
}
