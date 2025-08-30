package team.mjk.agent.domain.mcp.exception;

import team.mjk.agent.global.exception.CustomException;

public class McpNotFoundException extends CustomException {


  public McpNotFoundException(){
    super(McpExceptionCode.NOT_FOUND_MCP_EXCEPTION);
  }

}