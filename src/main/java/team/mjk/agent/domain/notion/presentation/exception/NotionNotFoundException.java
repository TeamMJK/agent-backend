package team.mjk.agent.domain.notion.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class NotionNotFoundException extends CustomException {

  public NotionNotFoundException() {
    super(NotionExceptionCode.NOTION_NOT_FOUND);
  }

}