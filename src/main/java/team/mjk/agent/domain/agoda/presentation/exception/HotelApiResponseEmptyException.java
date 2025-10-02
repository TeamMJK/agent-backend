package team.mjk.agent.domain.agoda.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class HotelApiResponseEmptyException extends CustomException {
    public HotelApiResponseEmptyException() {
        super(AgodaExceptionCode.HOTEL_API_RESPONSE_EMPTY);
    }
}
