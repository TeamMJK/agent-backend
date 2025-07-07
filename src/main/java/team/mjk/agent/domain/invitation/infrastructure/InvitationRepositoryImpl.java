package team.mjk.agent.domain.invitation.infrastructure;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.mjk.agent.domain.invitation.domain.Invitation;
import team.mjk.agent.domain.invitation.domain.InvitationRepository;

@RequiredArgsConstructor
@Repository
public class InvitationRepositoryImpl implements InvitationRepository {

  private final InvitationRedisRepository invitationRedisRepository;

  @Override
  public void save(Invitation invitation) {
    invitationRedisRepository.save(invitation);
  }

  @Override
  public Optional<Invitation> findByCode(String code) {
    return invitationRedisRepository.findInvitationByCode(code);
  }

  @Override
  public void delete(Invitation invitation) {
    invitationRedisRepository.delete(invitation);
  }

}