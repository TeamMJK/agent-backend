package team.mjk.agent.domain.passport.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import team.mjk.agent.domain.passport.domain.Passport;

import java.util.Optional;

public interface PassportJpaRepository extends JpaRepository<Passport, Long> {

    Optional<Passport> findByMemberId(Long memberId);

}
