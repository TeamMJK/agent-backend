package team.mjk.agent.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;
import team.mjk.agent.domain.businessTrip.application.BusinessTripService;
import team.mjk.agent.domain.businessTrip.presentation.request.BusinessTripAgentRequest;
import team.mjk.agent.domain.vnc.application.VncCacheService;
import team.mjk.agent.domain.vnc.domain.VncStatus;
import team.mjk.agent.domain.vnc.dto.VncResponse;
import team.mjk.agent.domain.vnc.presentation.exception.EndAgentExceptionCode;
import team.mjk.agent.domain.vnc.presentation.exception.FailAgentExceptionCode;
import team.mjk.agent.domain.vnc.presentation.exception.NotFoundAgentSessionExceptionCode;
import team.mjk.agent.domain.vnc.presentation.exception.NullAgentExceptionCode;
import team.mjk.agent.global.config.AgentUrlConfig;

@Slf4j
@RequiredArgsConstructor
@Component
public class AgentResponseUtil {

  private final WebClient webClient;
  private final ObjectMapper objectMapper;
  private final BusinessTripService businessTripService;
  private final AgentUrlConfig agentUrlConfig;
  private final VncCacheService vncCacheService;

  @Value("${agent.urls.session}")
  private String session;

  public void pauseAgent(String sessionId, VncStatus status) {
    String pythonUrlAgent;
    String baseSessionUrl = session;

    if (status.equals(VncStatus.PAUSE)) {
      pythonUrlAgent = baseSessionUrl + "/" + sessionId + "/pause";
    } else if (status.equals(VncStatus.ING)) {
      pythonUrlAgent = baseSessionUrl + "/" + sessionId + "/unpause";
    } else {
      throw new EndAgentExceptionCode();
    }

    try {
      WebClient http11WebClient = WebClient.builder()
              .clientConnector(new ReactorClientHttpConnector(
                      HttpClient.create().protocol(HttpProtocol.HTTP11)
              ))
              .build();

      http11WebClient.post()
              .uri(pythonUrlAgent)
              .accept(MediaType.APPLICATION_JSON)
              .retrieve()
              .bodyToMono(String.class)
              .block();
    } catch (Exception e) {
      log.error("세션 중단 요청 실패: {}", e.getMessage());
      throw new NotFoundAgentSessionExceptionCode();
    }
  }

  public VncResponse agentVnc(String pythonUrl, Map<String, Object> payload) {
    try {
      String jsonPayload = objectMapper.writeValueAsString(payload);
      log.info("JSON으로 변환된 payload: {}", jsonPayload);

      WebClient http11WebClient = WebClient.builder()
              .clientConnector(new ReactorClientHttpConnector(
                      HttpClient.create().protocol(HttpProtocol.HTTP11)
              ))
              .build();

      String responseResult = http11WebClient.post()
              .uri(pythonUrl)
              .contentType(MediaType.APPLICATION_JSON)
              .bodyValue(jsonPayload)
              .retrieve()
              .bodyToMono(String.class)
              .block();

      log.info("응답 받음: {}", responseResult);

      if (responseResult != null) {
        VncResponse response = objectMapper.readValue(responseResult, VncResponse.class);

        log.info("Session ID: {}", response.session_id());
        log.info("NoVNC URL: {}", response.novnc_url());
        log.info("Hotel Destination: {}", response.vncBusinessInfo().hotel_destination());
        log.info("Booking Dates: {}", response.vncBusinessInfo().booking_dates());
        log.info("Guests: {}", response.vncBusinessInfo().guests());
        log.info("Budget: {}", response.vncBusinessInfo().budget());

        return response;
      } else {
        log.warn("응답이 null입니다");
        return null;
      }

    } catch (Exception e) {
      log.error("요청 처리 중 오류: {}", e.getMessage(), e);
      throw new NullAgentExceptionCode();
    }
  }

  public void agentResponse(Long memberId, String pythonUrl, Map<String, Object> payload) {
    try {
      String jsonPayload = objectMapper.writeValueAsString(payload);
      log.info("JSON으로 변환된 payload: {}", jsonPayload);

      WebClient http11WebClient = WebClient.builder()
              .clientConnector(new ReactorClientHttpConnector(
                      HttpClient.create().protocol(HttpProtocol.HTTP11)
              ))
              .build();

      http11WebClient.post()
              .uri(pythonUrl)
              .contentType(MediaType.APPLICATION_JSON)
              .bodyValue(jsonPayload)
              .retrieve()
              .bodyToMono(String.class)
              .subscribe(responseResult -> {
                String sessionId = null;
                log.info("응답 받음: {}", responseResult);
                try {
                  JsonNode rootNode = objectMapper.readTree(responseResult);
                  JsonNode detailNode = rootNode.get("detail");
                  JsonNode sessionIdNode = rootNode.get("sessionId");
                  sessionId = sessionIdNode != null ? sessionIdNode.asText() : null;

                  if (detailNode != null) {
                    String detailJson = detailNode.toString();
                    log.info("result: {}", responseResult);
                    BusinessTripAgentRequest agentRequest = objectMapper.readValue(detailJson, BusinessTripAgentRequest.class);
                    log.info("names: {}", agentRequest.names());
                    businessTripService.saveAgentMcp(memberId, agentRequest);
                  } else {
                    log.warn("ERROR: detailNode is null in response: {}", responseResult);
                  }
                } catch (JsonProcessingException e) {
                  log.error("JSON 파싱 오류: {}", e.getMessage(), e);
                  throw new FailAgentExceptionCode();
                } finally {
                  if (sessionId != null) {
                    vncCacheService.updateVncStatus(memberId, sessionId, VncStatus.END);
                  }
                }
              }, error -> {
                log.error("WebClient error: {}", error.getMessage(), error);
                try {
                  JsonNode rootNode = objectMapper.readTree(error.getMessage());
                  JsonNode sessionIdNode = rootNode.get("sessionId");
                  if (sessionIdNode != null) {
                    String sessionId = sessionIdNode.asText();
                    vncCacheService.updateVncStatus(memberId, sessionId, VncStatus.END);
                  }
                } catch (JsonProcessingException ex) {
                  log.error("에러 메시지 JSON 파싱 실패: {}", ex.getMessage(), ex);
                }
              });

    } catch (JsonProcessingException e) {
      log.error("JSON 변환 실패: {}", e.getMessage(), e);
    }
  }
}
