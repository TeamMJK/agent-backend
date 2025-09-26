package team.mjk.agent.domain.vnc.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class NullAgentExceptionCode extends CustomException {

 public NullAgentExceptionCode() {
   super(VncExceptionCode.NULL_AGENT_EXCEPTION);
 }

}