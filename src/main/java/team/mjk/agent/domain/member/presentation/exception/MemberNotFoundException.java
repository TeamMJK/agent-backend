package team.mjk.agent.domain.member.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class MemberNotFoundException extends CustomException {
    public MemberNotFoundException() {
        super(MemberExceptionCode.MEMBER_NOT_FOUND);
    }
}
