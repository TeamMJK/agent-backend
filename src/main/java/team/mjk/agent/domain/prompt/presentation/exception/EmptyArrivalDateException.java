package team.mjk.agent.domain.prompt.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class EmptyArrivalDateException extends CustomException {

  public EmptyArrivalDateException(){
    super(PromptExceptionCode.EMPTY_ARRIVAL_DATE_EXCEPTION);
  }

}