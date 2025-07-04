package team.mjk.agent.domain.passport.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import team.mjk.agent.domain.passport.domain.Passport;

public interface PassportJpaRepository extends JpaRepository<Passport, Long> {

}
