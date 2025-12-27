package team.mjk.agent.domain.businessTrip.domain;

import jakarta.persistence.*;
import lombok.*;
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

    public void update(
            LocalDate departDate,
            LocalDate arriveDate,
            String destination,
            List<String> names,
            ServiceType serviceType
    ) {
        this.departDate = departDate;
        this.arriveDate = arriveDate;
        this.destination = destination;
        this.names = names;
        this.serviceType = serviceType;
    }

}