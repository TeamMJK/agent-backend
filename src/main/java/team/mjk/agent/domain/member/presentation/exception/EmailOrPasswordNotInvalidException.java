package team.mjk.agent.domain.member.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class EmailOrPasswordNotInvalidException extends CustomException {
    public EmailOrPasswordNotInvalidException() {
        super(MemberExceptionCode.EMAIL_OR_PASSWORD_NOT_INVALID);
    }
}
