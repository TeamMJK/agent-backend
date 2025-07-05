package team.mjk.agent.domain.invitation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.invitation.entity.Invitation;
import team.mjk.agent.domain.invitation.repository.InvitationRedisRepository;

@Transactional
@SpringBootTest
class InvitationCodeProviderTest {

    @InjectMocks
    private InvitationCodeProvider invitationCodeProvider;

    @Mock
    private InvitationRedisRepository invitationRedisRepository;


    @DisplayName("초대 코드를 생성한다")
    @Test
    void create() {
        Invitation invitation = invitationCodeProvider.create(1L);

        assertThat(invitation).isNotNull();
        assertThat(invitation.getCode()).isNotNull();

        verify(invitationRedisRepository).save(invitation);

    }

}