package team.mjk.agent.domain.prompt.presentation.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import team.mjk.agent.global.exception.ExceptionCode;

@Getter
@AllArgsConstructor
public enum PromptExceptionCode implements ExceptionCode {

  EMPTY_DEPARTURE_DATE_EXCEPTION(BAD_REQUEST,"가는 날짜 정보가 없습니다."),
  EMPTY_ARRIVAL_DATE_EXCEPTION(BAD_REQUEST,"오는 날짜 정보가 없습니다."),
  EMPTY_DESTINATION_EXCEPTION(BAD_REQUEST, "목적지 정보가 없습니다."),
  EMPTY_DEPARTURE_EXCEPTION(BAD_REQUEST, "출발지 정보가 없습니다."),
  ;

  private final HttpStatus status;
  private final String message;

  @Override
  public String getCode() {
    return this.name();
  }

}