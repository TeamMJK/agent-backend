package team.mjk.agent.domain.notion.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import team.mjk.agent.domain.notion.domain.Notion;
import team.mjk.agent.domain.notion.presentation.exception.NotionAPIException;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class NotionProvider {

    private final WebClient notionWebClient;

    public void send(Map<String, Object> payload, Notion notion, String token) {
        notionWebClient.post()
                .uri("/pages")
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .bodyValue(payload)
                .retrieve()
                .onStatus(
                        status -> !status.is2xxSuccessful(),
                        response -> Mono.error(new NotionAPIException())
                )
                .bodyToMono(Void.class)
                .block();
    }

}

