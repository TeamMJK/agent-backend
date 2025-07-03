package team.mjk.agent.domain.member.domain;

import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    boolean existsByEmail(String email);

    Optional<Member> findByMemberId(Long memberId);

    Optional<Member> findByEmail(String email);

}
