package team.mjk.agent.domain.vnc.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class NotFoundAgentSessionExceptionCode extends CustomException {

  public NotFoundAgentSessionExceptionCode() {
    super(VncExceptionCode.NOT_FOUND_AGENT_SESSION_EXCEPTION);
  }

}
