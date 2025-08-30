package team.mjk.agent.global.mcp.exception;

import team.mjk.agent.global.exception.CustomException;
import team.mjk.agent.global.exception.ExceptionCode;

public class McpNotFoundException extends CustomException {


  public McpNotFoundException(){
    super(McpExceptionCode.NOT_FOUND_MCP_EXCEPTION);
  }

}