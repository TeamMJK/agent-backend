package team.mjk.agent.domain.member.presentation.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import team.mjk.agent.global.exception.ExceptionCode;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@RequiredArgsConstructor
public enum MemberExceptionCode implements ExceptionCode {
    MEMBER_NOT_FOUND(NOT_FOUND, "회원을 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS(CONFLICT, "이메일이 이미 존재합니다."),
    ;

    private final HttpStatus status;
    private final String message;

    @Override
    public String getCode() {
        return this.name();
    }
}
