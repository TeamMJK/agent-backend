package team.mjk.agent.domain.member.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class EmailAlreadyExistsException extends CustomException {
    public EmailAlreadyExistsException() {
        super(MemberExceptionCode.EMAIL_ALREADY_EXISTS);
    }
}
