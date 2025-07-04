package team.mjk.agent.domain.businessTrip.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import team.mjk.agent.domain.businessTrip.domain.BusinessTrip;

public interface BusinessJpaRepository extends JpaRepository<BusinessTrip, Long> {

}