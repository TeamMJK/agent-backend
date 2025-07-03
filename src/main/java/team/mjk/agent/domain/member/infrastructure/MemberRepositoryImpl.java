package team.mjk.agent.domain.member.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;

@RequiredArgsConstructor
@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Member save(Member member) {
        return memberJpaRepository.save(member);
    }

    @Override
    public boolean existsByEmail(String email) {
        return memberJpaRepository.existsByEmail(email);
    }

}
