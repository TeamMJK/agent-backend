package team.mjk.agent.global.mcp.exception;



import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import team.mjk.agent.global.exception.ExceptionCode;

@Getter
@AllArgsConstructor
public enum McpExceptionCode implements ExceptionCode {

  NOT_FOUND_MCP_EXCEPTION(NOT_FOUND,"지원하지 않는 워크스페이스입니다.")
  ;

  private final HttpStatus status;
  private final String message;

  @Override
  public String getCode(){
    return this.name();
  }

}