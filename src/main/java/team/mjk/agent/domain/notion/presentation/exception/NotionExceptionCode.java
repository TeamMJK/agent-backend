package team.mjk.agent.domain.notion.presentation.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import team.mjk.agent.global.exception.ExceptionCode;

@Getter
@RequiredArgsConstructor
public enum NotionExceptionCode implements ExceptionCode {
  NOTION_NOT_FOUND(NOT_FOUND, "등록된 노션 정보가 없습니다."),
  NOTION_API_EXCEPTION(NOT_FOUND, "존재하지 않은 API 입니다.")
  ;

  private final HttpStatus status;
  private final String message;

  @Override
  public String getCode() {
    return this.name();
  }

}