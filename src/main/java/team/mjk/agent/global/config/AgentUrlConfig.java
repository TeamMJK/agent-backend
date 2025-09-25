package team.mjk.agent.global.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "agent.urls")
@Getter
public class AgentUrlConfig {

    private String hotelAgent;

    private String hotelSession;

}
