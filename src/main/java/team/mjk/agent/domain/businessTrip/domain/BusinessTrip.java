package team.mjk.agent.domain.businessTrip.domain;

import jakarta.persistence.*;
import lombok.*;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripUpdateRequest;
import team.mjk.agent.global.domain.BaseTimeEntity;

import java.time.LocalDate;
import java.util.List;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class BusinessTrip extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate departDate;

    private LocalDate arriveDate;

    private String destination;

    private ServiceType serviceType;

    private String writer;

    private Long companyId;

    @Convert(converter = NameListConverter.class)
    private List<String> names;

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

    public void update(BusinessTripUpdateRequest request) {
        this.departDate = request.departDate();
        this.arriveDate = request.arriveDate();
        this.destination = request.destination();
        this.names = request.names();
        this.serviceType = request.serviceType();
    }

}