package team.mjk.agent.domain.agoda.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class HotelInfoNotFoundException extends CustomException {
    public HotelInfoNotFoundException() {
        super(AgodaExceptionCode.HOTEL_INFO_NOT_FOUND);
    }
}
