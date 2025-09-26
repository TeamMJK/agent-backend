package team.mjk.agent.domain.vnc.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class FailAgentExceptionCode extends CustomException {

  public FailAgentExceptionCode() {
    super(VncExceptionCode.FAIL_AGENT_EXCEPTION);
  }

}
