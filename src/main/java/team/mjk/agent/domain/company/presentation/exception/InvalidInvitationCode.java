package team.mjk.agent.domain.company.presentation.exception;


import team.mjk.agent.global.exception.CustomException;

public class InvalidInvitationCode extends CustomException {

  public InvalidInvitationCode() {
    super(CompanyExceptionCode.INVALID_INVITATION_CODE);
  }

}
