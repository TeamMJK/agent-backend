package team.mjk.agent.domain.agoda.application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team.mjk.agent.domain.agoda.dto.request.AgodaHotelRequest;
import team.mjk.agent.domain.agoda.dto.response.AgodaHotelResponse;
import team.mjk.agent.domain.agoda.presentation.exception.HotelApiResponseEmptyException;
import team.mjk.agent.global.config.AgodaApiConfig;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RequiredArgsConstructor
@Service
public class AgodaHotelApiClient {

    private final AgodaApiConfig agodaApiConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    public AgodaHotelResponse fetchHotels(AgodaHotelRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        String auth = agodaApiConfig.siteId() + ":" + agodaApiConfig.apiKey();
        headers.set("Authorization", auth);

        HttpEntity<AgodaHotelRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<AgodaHotelResponse> response = restTemplate.exchange(
                agodaApiConfig.url(),
                POST,
                entity,
                AgodaHotelResponse.class
        );

        if (response.getBody() == null) {
            throw new HotelApiResponseEmptyException();
        }

        return response.getBody();
    }

}

