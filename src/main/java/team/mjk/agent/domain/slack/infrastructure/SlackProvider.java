package team.mjk.agent.domain.slack.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class SlackProvider {

    private final WebClient slackWebClient;

    public void sendMessage(String token, String channelId, String message) {

        slackWebClient.post()
                .uri("/chat.postMessage")
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json; charset=utf-8")
                .bodyValue(Map.of(
                        "channel", channelId,
                        "text", message
                ))
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> System.err.println("Slack API 실패: " + e.getMessage()))
                .block();
    }
}
