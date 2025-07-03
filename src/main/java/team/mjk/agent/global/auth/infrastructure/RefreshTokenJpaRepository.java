package team.mjk.agent.global.auth.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import team.mjk.agent.global.auth.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findFirstByMemberIdOrderByIdDesc(Long id);

    void deleteByMemberId(Long memberId);

}
