package team.mjk.agent.domain.review.presentation.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import team.mjk.agent.global.exception.ExceptionCode;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum ReviewExceptionCode implements ExceptionCode {
    REVIEW_NOT_FOUND(NOT_FOUND, "회원님의 리뷰를 찾을 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;

    @Override
    public String getCode() {
        return this.name();
    }
}
