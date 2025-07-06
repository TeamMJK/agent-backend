package team.mjk.agent.domain.invitation.infrastructure;

import io.jsonwebtoken.security.Jwks.OP;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import team.mjk.agent.domain.invitation.domain.Invitation;

public interface InvitationRedisRepository extends CrudRepository<Invitation, String> {

  Optional<Invitation> findInvitationByCode(String code);

}