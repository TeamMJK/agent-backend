package team.mjk.agent.domain.invitation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import team.mjk.agent.domain.invitation.domain.Invitation;
import team.mjk.agent.domain.invitation.infrastructure.InvitationRepositoryImpl;

@Slf4j
@RequiredArgsConstructor
@Service
public class InvitationCodeProvider {

    private final InvitationRepositoryImpl invitationRepositoryImpl;

    private static final long DEFAULT_EXPIRED_MINUTES = 60L;
    private static final int INVITE_CODE_LENGTH = 32;

    public Invitation create(Long companyId) {
        String code = RandomStringUtils.randomAlphanumeric(INVITE_CODE_LENGTH);

        Invitation invitation = Invitation.create(companyId, code, DEFAULT_EXPIRED_MINUTES);

        invitationRepositoryImpl.save(invitation);

        log.info("Company {} , 초대 코드 생성: {}", companyId, invitation.getCode());

        return invitation;
    }

}