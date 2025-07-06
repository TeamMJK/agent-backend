package team.mjk.agent.domain.invitation.domain;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "invitation")
public class Invitation {

    @Id
    private String id;

    @Indexed
    private Long companyId;

    @Indexed
    private String code;

    @TimeToLive(unit = TimeUnit.MINUTES) // 60 m
    private Long expiration;

    public static Invitation create(Long companyId, String code, Long expirationInMinutes) {
        Invitation invitation = new Invitation();

        invitation.id = "company " + requireNonNull(code);
        invitation.companyId =  requireNonNull(companyId);
        invitation.code = code;
        invitation.expiration = requireNonNull(expirationInMinutes);

        return invitation;
    }

}