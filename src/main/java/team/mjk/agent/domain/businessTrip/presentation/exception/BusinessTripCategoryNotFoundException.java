package team.mjk.agent.domain.businessTrip.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class BusinessTripCategoryNotFoundException extends CustomException {

  public BusinessTripCategoryNotFoundException() {
    super(BusinessTripExceptionCode.NOT_FOUND_CATEGORY);
  }

}