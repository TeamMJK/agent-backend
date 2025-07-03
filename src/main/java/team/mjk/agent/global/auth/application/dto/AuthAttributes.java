package team.mjk.agent.global.auth.application.dto;

import team.mjk.agent.domain.member.domain.LoginProvider;

import java.util.Map;

public interface AuthAttributes {

    String getExternalId();

    String getEmail();

    LoginProvider getProvider();

    static AuthAttributes of(String providerId, Map<String, Object> attributes) {
        if (LoginProvider.google.isProviderOf(providerId)) {
            return GoogleAuthAttributes.of(attributes);
        }
        throw new IllegalArgumentException("Unsupported id: " + providerId);
    }
}
