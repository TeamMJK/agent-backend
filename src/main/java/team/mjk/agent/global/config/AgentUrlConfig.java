package team.mjk.agent.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "agent.urls")
public record AgentUrlConfig(
        String hotelAgent,
        String hotelSession,
        String session
) {

}

