package team.mjk.agent.domain.member.domain;

import java.util.Objects;

public enum LoginProvider {
    google;

    public boolean isProviderOf(String providerId) {
        return Objects.equals(this.name(), providerId);
    }

}
