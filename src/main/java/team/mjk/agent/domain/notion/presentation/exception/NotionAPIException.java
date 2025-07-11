package team.mjk.agent.domain.notion.presentation.exception;

import team.mjk.agent.global.exception.CustomException;
import team.mjk.agent.global.exception.ExceptionCode;

public class NotionAPIException extends CustomException {

  public NotionAPIException() {
    super(NotionExceptionCode.NOTION_API_EXCEPTION);
  }

  public NotionAPIException(Throwable cause) {
    super(NotionExceptionCode.NOTION_API_EXCEPTION, cause);
  }

}
