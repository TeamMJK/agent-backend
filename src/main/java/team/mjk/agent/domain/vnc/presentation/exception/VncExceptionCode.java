package team.mjk.agent.domain.vnc.presentation.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import team.mjk.agent.global.exception.ExceptionCode;

@Getter
@AllArgsConstructor
public enum VncExceptionCode implements ExceptionCode {
 END_AGENT_EXCEPTION(BAD_REQUEST, "이미 종료된 세션 입니다."),
 NULL_AGENT_EXCEPTION(INTERNAL_SERVER_ERROR, "Agent 서버가 꺼져있습니다. "),
 FAIL_AGENT_EXCEPTION(INTERNAL_SERVER_ERROR, "예약에 실패했습니다."),
 Fail_Redis_Agent_Connect_Exception(INTERNAL_SERVER_ERROR, "Redis 연결에 실패했습니다."),
 NOT_FOUND_AGENT_SESSION_EXCEPTION(NOT_FOUND, "유효하지 않은 Agent 입니다.")
 ,
 ;

  private final HttpStatus status;
  private final String message;

  @Override
  public String getCode() {
    return this.name();
  }
}