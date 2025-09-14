package team.mjk.agent.domain.prompt.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class EmptyDestinationException extends CustomException {

  public EmptyDestinationException() {
    super(PromptExceptionCode.EMPTY_DESTINATION_EXCEPTION);
  }

}