package team.mjk.agent.domain.vnc.domain;

import java.util.List;
import team.mjk.agent.domain.member.domain.Member;

public interface VncRepository {

  Vnc save(Vnc vnc);

  List<Vnc> findAllByMember(Member member);
}