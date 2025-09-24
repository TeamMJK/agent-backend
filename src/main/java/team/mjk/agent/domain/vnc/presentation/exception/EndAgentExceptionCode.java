package team.mjk.agent.domain.vnc.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class EndAgentExceptionCode extends CustomException {

 public EndAgentExceptionCode(){
   super(VncExceptionCode.END_AGENT_EXCEPTION);
 }

}