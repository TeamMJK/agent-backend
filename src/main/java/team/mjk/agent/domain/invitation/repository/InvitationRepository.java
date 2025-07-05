package team.mjk.agent.domain.invitation.repository;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.mjk.agent.domain.invitation.entity.Invitation;

@RequiredArgsConstructor
@Repository
public class InvitationRepository {

    private final InvitationRedisRepository invitationRedisRepository;

    public Optional<Invitation> findByCompanyId(Long teamId) {
        return invitationRedisRepository.findByCompanyId(teamId);
    }

    // TODO: 예외 추가해주세요.
    public Invitation findByCode(String code) {
        return invitationRedisRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않는 코드입니다"));
    }

    public void save(Invitation invitation) {
        invitationRedisRepository.save(invitation);
    }

}