package team.mjk.agent.domain.invitation.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import team.mjk.agent.domain.invitation.entity.Invitation;

public interface InvitationRedisRepository extends CrudRepository<Invitation, String> {
    Optional<Invitation> findByCompanyId(Long companyId);
    Optional<Invitation> findByCode(String code);

}