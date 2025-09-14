package team.mjk.agent.domain.prompt.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class EmptyDepartureDateExceptionCode extends CustomException {

  public EmptyDepartureDateExceptionCode(){
    super(PromptExceptionCode.EMPTY_DEPARTURE_DATE_EXCEPTION);
  }

}
