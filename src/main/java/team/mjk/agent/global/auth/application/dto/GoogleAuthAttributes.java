package team.mjk.agent.global.auth.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import team.mjk.agent.domain.member.domain.LoginProvider;

import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GoogleAuthAttributes implements AuthAttributes {

    private final String id;
    private final String email;
    private final LoginProvider provider;

    public static GoogleAuthAttributes of(Map<String, Object> attributes) {
        return new GoogleAuthAttributes(
                attributes.get("sub").toString(),
                (String) attributes.get("email"),
                LoginProvider.google
        );
    }

    @Override
    public String getExternalId() {
        return this.id;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public LoginProvider getProvider() {
        return this.provider;
    }

}
