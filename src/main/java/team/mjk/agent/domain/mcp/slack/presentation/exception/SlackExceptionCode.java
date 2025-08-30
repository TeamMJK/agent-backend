package team.mjk.agent.domain.mcp.slack.presentation.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import team.mjk.agent.global.exception.ExceptionCode;

@Getter
@RequiredArgsConstructor
public enum SlackExceptionCode implements ExceptionCode {

  SLACK_NOT_FOUND(NOT_FOUND,"등록된 슬랙 정보가 없습니다."),
  SLACK_API_EXCEPTION(NOT_FOUND, "존재하지 않은 API 입니다.")
  ;

  private final HttpStatus status;
  private final String message;

  @Override
  public String getCode(){
    return this.name();
  }

}
