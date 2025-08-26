package team.mjk.agent.domain.businessTrip.presentation.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import team.mjk.agent.global.exception.ExceptionCode;

@Getter
@RequiredArgsConstructor
public enum BusinessTripExceptionCode implements ExceptionCode {
  BUSINESS_TRIP_NOT_FOUND(NOT_FOUND,"출장 정보를 찾을 수 없습니다"),
  NOT_FOUND_CATEGORY(NOT_FOUND,"해당 카테고리를 찾을 수 없습니다.");

  private final HttpStatus status;
  private final String message;

  @Override
  public String getCode() {
    return this.name();
  }

}