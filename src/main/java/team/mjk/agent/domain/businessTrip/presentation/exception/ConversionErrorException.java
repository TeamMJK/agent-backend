package team.mjk.agent.domain.businessTrip.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class ConversionErrorException extends CustomException {

    public ConversionErrorException() {
        super(BusinessTripExceptionCode.CONVERSION_ERROR);
    }

}
