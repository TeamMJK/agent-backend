package team.mjk.agent.domain.agoda.presentation.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import team.mjk.agent.global.exception.ExceptionCode;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@RequiredArgsConstructor
public enum AgodaExceptionCode implements ExceptionCode {
    CITY_CSV_FORMAT(INTERNAL_SERVER_ERROR, "잘못된 도시 CSV 데이터 형식입니다."),
    CITY_NOT_FOUND(NOT_FOUND, "요청한 도시에 대한 정보를 찾을 수 없습니다."),
    HOTEL_API_RESPONSE_EMPTY(INTERNAL_SERVER_ERROR, "Agoda API 응답이 비어 있습니다."),
    HOTEL_INFO_NOT_FOUND(INTERNAL_SERVER_ERROR, "호텔 정보가 추출되지 않았거나 목적지 도시 정보를 찾을 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;

    @Override
    public String getCode() {
        return this.name();
    }
}
