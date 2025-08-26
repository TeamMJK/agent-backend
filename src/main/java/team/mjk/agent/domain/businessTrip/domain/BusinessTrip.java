package team.mjk.agent.domain.businessTrip.domain;

import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripUpdateRequest;
import team.mjk.agent.global.domain.BaseTimeEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BusinessTrip {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDate departDate;

  private LocalDate arriveDate;

  private String destination;

  private ServiceType serviceType;

  private String writer;

  private Long companyId;

  @ElementCollection
  private List<String> names;

  @Builder
  private BusinessTrip(
      LocalDate departDate,
      LocalDate arriveDate,
      String destination,
      List<String> names,
      String writer,
      Long companyId,
      ServiceType serviceType
  ) {
    this.destination = destination;
    this.departDate = departDate;
    this.arriveDate = arriveDate;
    this.names = names;
    this.writer = writer;
    this.companyId = companyId;
    this.serviceType = serviceType;
  }

  public void update(BusinessTripUpdateRequest request) {
    this.departDate = request.departDate();
    this.arriveDate =  request.arriveDate();
    this.destination = request.destination();
    this.names = request.names();
    this.serviceType = request.serviceType();
  }

  public static BusinessTrip create(
      LocalDate departDate,
      LocalDate arriveDate,
      String destination,
      List<String> names,
      String writer,
      Long companyId,
      ServiceType serviceType
  ) {
    return BusinessTrip.builder()
        .arriveDate(arriveDate)
        .departDate(departDate)
        .destination(destination)
        .names(names)
        .writer(writer)
        .companyId(companyId)
        .serviceType(serviceType)
        .build();
  }

}