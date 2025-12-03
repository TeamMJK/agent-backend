package team.mjk.agent.domain.company.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public static Company create(String name) {
        return Company.builder()
                .name(name)
                .build();
    }

    public void updateName(String name) {
        this.name = name;
    }

}