package team.mjk.agent.domain.prompt.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class EmptyDepartureException extends CustomException {

  public EmptyDepartureException() {
    super(PromptExceptionCode.EMPTY_DEPARTURE_EXCEPTION);
  }

}