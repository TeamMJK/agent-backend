package team.mjk.agent.domain.company.presentation.exception;

import team.mjk.agent.global.exception.CustomException;


public class CompanyNotFoundException extends CustomException {

  public CompanyNotFoundException() {
    super(CompanyExceptionCode.NOT_FOUND_COMPANY);
  }
}
