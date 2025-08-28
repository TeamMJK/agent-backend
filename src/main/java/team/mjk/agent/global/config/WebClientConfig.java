package team.mjk.agent.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

  @Bean
  public WebClient webClient() {
    return WebClient.builder().build();
  }

  @Bean
  public WebClient slackWebClient() {
    return WebClient.builder()
        .baseUrl("https://slack.com/api")
        .build();
  }

  @Bean
  public WebClient notionWebClient() {
    return WebClient.builder()
        .baseUrl("https://api.notion.com/v1")
        .defaultHeader("Notion-Version", "2022-06-28")
        .build();
  }

}