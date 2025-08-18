package team.mjk.agent.domain.prompt.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import team.mjk.agent.domain.prompt.application.PromptService;
import team.mjk.agent.domain.prompt.dto.request.PromptRequest;
import team.mjk.agent.domain.prompt.dto.response.BusinessTripAndMemberInfoResponse;
import team.mjk.agent.domain.prompt.dto.response.HotelAndMemberInfoResponse;
import team.mjk.agent.domain.prompt.dto.response.IntegrationResponse;
import team.mjk.agent.global.annotation.MemberId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/prompts")
public class PromptController {

  private final PromptService promptService;
  private final RestTemplate restTemplate = new RestTemplate();

  @PostMapping("/hotel")
  public void extractHotelPrompt(
      @MemberId Long memberId,
      @Valid @RequestBody PromptRequest request
  ) {
    HotelAndMemberInfoResponse response = promptService.extractHotel(memberId, request);

    String pythonUrl = "http://localhost:8000/hotel-data";
    System.out.println(response);
    restTemplate.postForObject(pythonUrl, response, String.class);
  }

  @PostMapping("/business-trip")
  public void extractBusinessTripPrompt(
      @MemberId Long memberId,
      @Valid @RequestBody PromptRequest request
  ) {
    BusinessTripAndMemberInfoResponse response = promptService.extractBusinessTrip(memberId,
        request);

    String pythonUrl = "http://localhost:8000/business-trip-data";

    restTemplate.postForObject(pythonUrl, response, String.class);
  }

  @PostMapping("/integration")
  public void extractIntegration(
      @MemberId Long memberId,
      @Valid @RequestBody PromptRequest request
  ) {
    IntegrationResponse response = promptService.extractIntegration(memberId, request);

    String pythonUrl = "http://localhost:8000/integration";

    restTemplate.postForObject(pythonUrl, response, String.class);
  }

}