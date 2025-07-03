package team.mjk.agent.domain.member.domain;

public interface MemberRepository {

    Member save(Member member);

    boolean existsByEmail(String email);

}
