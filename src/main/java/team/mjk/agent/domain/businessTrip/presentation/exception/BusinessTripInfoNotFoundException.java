package team.mjk.agent.domain.businessTrip.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class BusinessTripInfoNotFoundException extends CustomException {

  public BusinessTripInfoNotFoundException() {
    super(BusinessTripExceptionCode.BUSINESS_TRIP_NOT_FOUND);
  }

}

