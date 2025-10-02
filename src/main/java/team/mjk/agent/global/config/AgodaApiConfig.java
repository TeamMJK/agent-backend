package team.mjk.agent.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "agoda.api")
public record AgodaApiConfig(

        String url,
        String siteId,
        String apiKey

) {
}
