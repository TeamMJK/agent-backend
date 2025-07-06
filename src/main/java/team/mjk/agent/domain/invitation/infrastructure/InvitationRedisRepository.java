package team.mjk.agent.domain.invitation.infrastructure;

import org.springframework.data.repository.CrudRepository;
import team.mjk.agent.domain.invitation.domain.Invitation;

public interface InvitationRedisRepository extends CrudRepository<Invitation, String> {

}