package team.mjk.agent.domain.vnc.presentation.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import team.mjk.agent.global.exception.ExceptionCode;

@Getter
@AllArgsConstructor
public enum VncExceptionCode implements ExceptionCode {
 END_AGENT_EXCEPTION(BAD_REQUEST, "이미 종료된 세션 입니다.");

  private final HttpStatus status;
  private final String message;

  @Override
  public String getCode() {
    return this.name();
  }
}