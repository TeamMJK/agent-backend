package team.mjk.agent.domain.agoda.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class CityCsvFormatException extends CustomException {
    public CityCsvFormatException() {
        super(AgodaExceptionCode.CITY_CSV_FORMAT);
    }
}
