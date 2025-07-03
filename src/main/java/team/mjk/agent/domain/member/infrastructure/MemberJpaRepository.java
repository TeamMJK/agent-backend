package team.mjk.agent.domain.member.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import team.mjk.agent.domain.member.domain.Member;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

}
