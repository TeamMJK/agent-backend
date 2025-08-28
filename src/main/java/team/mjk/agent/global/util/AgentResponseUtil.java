package team.mjk.agent.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import team.mjk.agent.domain.businessTrip.application.BusinessTripService;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripAgentRequest;

@RequiredArgsConstructor
@Component
public class AgentResponseUtil {

  private final WebClient webClient;
  private final ObjectMapper objectMapper;
  private final BusinessTripService businessTripService;

  public void agentResponse(Long memberId, String pythonUrl, Map<String, Object> payload) {
    webClient.post()
        .uri(pythonUrl)
        .bodyValue(payload)
        .retrieve()
        .bodyToMono(String.class)
        .subscribe(responseResult -> {
          try {
            JsonNode rootNode = objectMapper.readTree(responseResult);
            JsonNode detailNode = rootNode.get("detail");
            String detailJson = detailNode.toString();
            System.out.println("result :" + responseResult);
            BusinessTripAgentRequest agentRequest = objectMapper.readValue(detailJson, BusinessTripAgentRequest.class);

            System.out.println("names" + agentRequest.names());
            businessTripService.saveAgentMcp(memberId, agentRequest);
          } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
          }
        }, error -> {
          System.out.println("WebClient error: " + error.getMessage());
        });
  }

}