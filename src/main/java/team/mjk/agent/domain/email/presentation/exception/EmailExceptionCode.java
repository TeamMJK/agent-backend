package team.mjk.agent.domain.email.presentation.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import team.mjk.agent.global.exception.ExceptionCode;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
@RequiredArgsConstructor
public enum EmailExceptionCode implements ExceptionCode {
    INVALID_CODE(BAD_REQUEST, "인증 코드가 유효하지 않습니다."),
    SEND_FAILED(INTERNAL_SERVER_ERROR, "이메일 전송에 실패했습니다.");
    ;

    private final HttpStatus status;

    private final String message;

    @Override
    public String getCode() {
        return this.name();
    }
}
