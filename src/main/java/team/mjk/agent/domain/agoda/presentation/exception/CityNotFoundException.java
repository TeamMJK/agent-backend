package team.mjk.agent.domain.agoda.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class CityNotFoundException extends CustomException {
    public CityNotFoundException() {
        super(AgodaExceptionCode.CITY_NOT_FOUND);
    }
}
