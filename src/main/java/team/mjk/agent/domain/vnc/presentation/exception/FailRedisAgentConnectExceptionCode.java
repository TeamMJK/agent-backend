package team.mjk.agent.domain.vnc.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class FailRedisAgentConnectExceptionCode extends CustomException {

  public FailRedisAgentConnectExceptionCode(){
    super(VncExceptionCode.Fail_Redis_Agent_Connect_Exception);
  }

}
