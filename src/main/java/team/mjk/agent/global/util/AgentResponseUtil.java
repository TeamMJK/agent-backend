package team.mjk.agent.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;
import team.mjk.agent.domain.businessTrip.application.BusinessTripService;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripAgentRequest;
import team.mjk.agent.domain.vnc.domain.VncStatus;
import team.mjk.agent.domain.vnc.dto.VncResponse;
import team.mjk.agent.domain.vnc.presentation.exception.EndAgentExceptionCode;
import team.mjk.agent.global.config.AgentUrlConfig;

@RequiredArgsConstructor
@Component
public class AgentResponseUtil {

  private final WebClient webClient;
  private final ObjectMapper objectMapper;
  private final BusinessTripService businessTripService;
  private final AgentUrlConfig agentUrlConfig;
  /**
  public void agentResponse(Long memberId, String pythonUrl, Map<String, Object> payload) {
    System.out.println("날라갑니다~: " +payload);
    webClient.post()
        .uri(pythonUrl)
        .header("Content-Type", "application/json")
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

public void agentResponse(Long memberId, String pythonUrl, Map<String, Object> payload) {
  try {
    String jsonPayload = objectMapper.writeValueAsString(payload);
    System.out.println("[DEBUG] Sending payload: " + jsonPayload);


    webClient.post()
        .uri(pythonUrl)
        .header("Content-Type", "application/json")
        .bodyValue(jsonPayload)
        .retrieve()
        .bodyToMono(String.class)
        .block();



  } catch (Exception e) {
    System.out.println("WebClient error: " + e.getMessage());
  }
}
**/

  public void pauseAgent(String sessionId, VncStatus status) {
    String pythonUrlAgent;
    String baseSessionUrl = agentUrlConfig.session();

    if (status.equals(VncStatus.PAUSE)) {
      pythonUrlAgent = baseSessionUrl + "/" + sessionId + "/pause";
    } else if (status.equals(VncStatus.ING)) {
      pythonUrlAgent = baseSessionUrl + "/" + sessionId + "/unpause";
    } else {
      throw new EndAgentExceptionCode();
    }

    WebClient http11WebClient = WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(
            HttpClient.create()
                .protocol(HttpProtocol.HTTP11)
        ))
        .build();

    String responseResult = http11WebClient.post()
        .uri(pythonUrlAgent)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(String.class)
        .block();

    System.out.println("응답 받음: " + responseResult);
  }


  public VncResponse agentVnc(String pythonUrl, Map<String, Object> payload) {
    try {
      String jsonPayload = objectMapper.writeValueAsString(payload);
      System.out.println("JSON으로 변환된 payload: " + jsonPayload);

      WebClient http11WebClient = WebClient.builder()
          .clientConnector(new ReactorClientHttpConnector(
              HttpClient.create()
                  .protocol(HttpProtocol.HTTP11)
          ))
          .build();

      String responseResult = http11WebClient.post()
          .uri(pythonUrl)
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(jsonPayload)
          .retrieve()
          .bodyToMono(String.class)
          .block();

      System.out.println("응답 받음: " + responseResult);

      if (responseResult != null) {
        // ObjectMapper로 직접 record 타입 매핑
        VncResponse response = objectMapper.readValue(responseResult, VncResponse.class);

        System.out.println("Session ID: " + response.session_id());
        System.out.println("NoVNC URL: " + response.novnc_url());
        System.out.println("Hotel Destination: " + response.vncBusinessInfo().hotel_destination());
        System.out.println("Booking Dates: " + response.vncBusinessInfo().booking_dates());
        System.out.println("Guests: " + response.vncBusinessInfo().guests());
        System.out.println("Budget: " + response.vncBusinessInfo().budget());

        return response;
      } else {
        System.out.println("응답이 null입니다");
        return null;
      }

    } catch (Exception e) {
      System.out.println("요청 처리 중 오류: " + e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

  public void agentResponse(Long memberId, String pythonUrl, Map<String, Object> payload) {
    try {
      String jsonPayload = objectMapper.writeValueAsString(payload);
      System.out.println("JSON으로 변환된 payload: " + jsonPayload);

      // HTTP/1.1만 사용하는 WebClient 생성
      WebClient http11WebClient = WebClient.builder()
          .clientConnector(new ReactorClientHttpConnector(
              HttpClient.create()
                  .protocol(HttpProtocol.HTTP11)  // HTTP/1.1만 사용
          ))
          .build();

      http11WebClient.post()
          .uri(pythonUrl)
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(jsonPayload)
          .retrieve()
          .bodyToMono(String.class)
          .subscribe(responseResult -> {
            System.out.println("응답 받음: " + responseResult);
            try {
              JsonNode rootNode = objectMapper.readTree(responseResult);
              JsonNode detailNode = rootNode.get("detail");
              if (detailNode != null) {
                String detailJson = detailNode.toString();
                System.out.println("result :" + responseResult);
                BusinessTripAgentRequest agentRequest = objectMapper.readValue(detailJson, BusinessTripAgentRequest.class);
                System.out.println("names" + agentRequest.names());
                businessTripService.saveAgentMcp(memberId, agentRequest);
              } else {
                System.out.println("ERROR: detailNode is null in response: " + responseResult);
              }
            } catch (JsonProcessingException e) {
              System.out.println(e.getMessage());
            }
          }, error -> {
            System.out.println("WebClient error: " + error.getMessage());
          });

    } catch (JsonProcessingException e) {
      System.out.println("JSON 변환 실패: " + e.getMessage());
    }
  }

}