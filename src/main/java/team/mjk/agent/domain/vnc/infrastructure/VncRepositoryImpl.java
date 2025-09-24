package team.mjk.agent.domain.vnc.infrastructure;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.vnc.domain.Vnc;
import team.mjk.agent.domain.vnc.domain.VncRepository;

@RequiredArgsConstructor
@Repository
public class VncRepositoryImpl implements VncRepository {

  private final VncJpaRepository vncJpaRepository;

  @Override
  public Vnc save(Vnc vnc) {
    return vncJpaRepository.save(vnc);
  }

  @Override
  public List<Vnc> findAllByMember(Member member) {
    return vncJpaRepository.findAllByMember(member);
  }

  @Override
  public Vnc findByMemberAndSessionId(Member member, String sessionId) {
    return vncJpaRepository.findByMemberAndSessionId(member,sessionId);
  }

}