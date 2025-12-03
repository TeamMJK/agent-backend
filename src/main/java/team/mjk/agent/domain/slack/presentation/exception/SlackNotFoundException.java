package team.mjk.agent.domain.slack.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class SlackNotFoundException extends CustomException {

  public SlackNotFoundException(){
    super(SlackExceptionCode.SLACK_NOT_FOUND);
  }

}