package team.mjk.agent.global.auth.domain;

import jakarta.persistence.*;
import lombok.*;
import team.mjk.agent.global.auth.presentation.exception.RefreshTokenNotValidException;
import team.mjk.agent.global.domain.BaseTimeEntity;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class RefreshToken extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id;

    private Long memberId;

    private String token;

    private LocalDateTime expiredAt;

    public static RefreshToken of(Long memberId, String token, long expiredSeconds) {
        LocalDateTime expiredAt = LocalDateTime.now().plusSeconds(expiredSeconds);

        return RefreshToken.builder()
                .memberId(memberId)
                .token(token)
                .expiredAt(expiredAt)
                .build();
    }

    public void rotate(String token) {
        this.token = token;
    }

    public void updateExpirationIfExpired(long expiredSeconds) {
        if (isExpired()) {
            expiredAt = LocalDateTime.now().plusSeconds(expiredSeconds);
        }
    }

    public void validateWith(String token, Long memberId) {
        if (isNotMatchedToken(token) || isExpired() || isNotMatchedUserId(memberId)) {
            throw new RefreshTokenNotValidException();
        }
    }

    private boolean isNotMatchedToken(String token) {
        return !Objects.equals(this.token, token);
    }

    private boolean isExpired() {
        return expiredAt.isBefore(LocalDateTime.now());
    }

    private boolean isNotMatchedUserId(Long memberId) {
        return !Objects.equals(this.memberId, memberId);
    }

}
