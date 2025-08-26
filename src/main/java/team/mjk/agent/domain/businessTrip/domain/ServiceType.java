package team.mjk.agent.domain.businessTrip.domain;

import lombok.Getter;
import team.mjk.agent.domain.businessTrip.presentation.exception.BusinessTripCategoryNotFoundException;

@Getter
public enum ServiceType {
  FLIGHT("항공"), HOTEL("숙박");
  private final String category;

  ServiceType(String category) {
    this.category = category;
  }

  public static ServiceType fromCategory(String category) {
    for (ServiceType type : ServiceType.values()) {
      if (type.category.equals(category)) {
        return type;
      }
    }
    throw new BusinessTripCategoryNotFoundException();
  }

}