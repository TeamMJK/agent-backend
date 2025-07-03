package team.mjk.agent.domain.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import team.mjk.agent.domain.member.presentation.exception.EmailOrPasswordNotInvalidException;
import team.mjk.agent.global.auth.application.dto.AuthAttributes;
import team.mjk.agent.global.domain.BaseTimeEntity;

import java.util.Objects;

import static jakarta.persistence.EnumType.STRING;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String externalId;

    @Enumerated(STRING)
    private LoginProvider loginProvider;

    @Builder
    private Member(String email, String password, String externalId, LoginProvider loginProvider) {
        this.email = email;
        this.password = password;
        this.externalId = externalId;
        this.loginProvider = loginProvider;
    }

    public void checkPassword(PasswordEncoder passwordEncoder, String password) {
        if (!passwordEncoder.matches(password, this.password)) {
            throw new EmailOrPasswordNotInvalidException();
        }
    }

    public static Member from(AuthAttributes authAttributes) {
        return Member.builder()
                .email(authAttributes.getEmail())
                .externalId(authAttributes.getExternalId())
                .loginProvider(authAttributes.getProvider())
                .build();
    }

    public boolean hasDifferentProviderWithEmail(String email, String externalId) {
        return Objects.equals(this.email, email) && !Objects.equals(this.externalId, externalId);
    }

}
