package team.mjk.agent.domain.invitation.domain;

import java.util.Optional;

public interface InvitationRepository {

    void save(Invitation invitation);
    Optional<Invitation> findByCode(String code);
    void delete(Invitation invitation);
}