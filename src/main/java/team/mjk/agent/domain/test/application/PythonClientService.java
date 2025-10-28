package team.mjk.agent.domain.test.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;

@Service
public class PythonClientService {

    private final WebClient http11WebClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PythonClientService() {
        this.http11WebClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create()
                                .protocol(HttpProtocol.HTTP11) // HTTP/1.1로 고정
                ))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public void sendPostToPython() {
        String url = "http://1.228.207.174:8000/test1";

        try {
            String response = http11WebClient.post()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // 동기식으로 응답 대기

            JsonNode jsonNode = objectMapper.readTree(response);
            String status = jsonNode.get("status").asText();

            System.out.println("✅ Python 서버 응답 status: " + status);
        } catch (Exception e) {
            System.err.println("❌ Python 서버 요청 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
