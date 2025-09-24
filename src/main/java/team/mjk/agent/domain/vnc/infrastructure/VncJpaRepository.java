package team.mjk.agent.domain.vnc.infrastructure;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.vnc.domain.Vnc;

public interface VncJpaRepository extends JpaRepository<Vnc, Long> {

  List<Vnc> findAllByMember(Member member);

}
