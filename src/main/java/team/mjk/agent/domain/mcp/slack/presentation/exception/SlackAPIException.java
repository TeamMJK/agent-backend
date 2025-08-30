package team.mjk.agent.domain.mcp.slack.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class SlackAPIException extends CustomException {

  public SlackAPIException(){
    super(SlackExceptionCode.SLACK_API_EXCEPTION);
  }

}