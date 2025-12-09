package team.mjk.agent.domain.notion.infrastructure;

import java.util.List;
import java.util.Map;

public class NotionPropertyBuilder {

    public static Map<String, Object> title(List<String> values) {
        return Map.of("title",
                values.stream()
                        .map(v -> Map.of("text", Map.of("content", v)))
                        .toList()
        );
    }

    public static Map<String, Object> richText(String value) {
        return Map.of("rich_text",
                List.of(Map.of("text", Map.of("content", value)))
        );
    }

    public static Map<String, Object> url(String url) {
        return Map.of("url", url);
    }

}

