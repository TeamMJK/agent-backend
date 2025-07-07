package team.mjk.agent.domain.company.presentation.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import team.mjk.agent.global.exception.ExceptionCode;

@Getter
@RequiredArgsConstructor
public enum CompanyExceptionCode implements ExceptionCode {

  INVALID_INVITATION_CODE(BAD_REQUEST, "유효하지 않은 초대 코드입니다."),
  NOT_FOUND_COMPANY(NOT_FOUND,"존재하지 않은 회사입니다.")
  ;


  private final HttpStatus status;
  private final String message;

  @Override
  public String getCode() {
    return this.name();
  }

}