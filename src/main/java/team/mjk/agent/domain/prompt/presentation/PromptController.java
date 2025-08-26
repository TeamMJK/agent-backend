package team.mjk.agent.domain.prompt.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.mjk.agent.domain.prompt.application.PromptService;
import team.mjk.agent.domain.prompt.dto.request.PromptRequest;
import team.mjk.agent.domain.prompt.dto.response.IntegrationResponse;
import team.mjk.agent.global.annotation.MemberId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/prompts")
public class PromptController {

  private final PromptService promptService;

  @PostMapping("/hotel")
  public void extractHotelPrompt(
      @MemberId Long memberId,
      @Valid @RequestBody PromptRequest request
  ) {
    promptService.handleHotel(memberId, request);
  }

  @PostMapping("/flight")
  public void extractFlightPrompt(
      @MemberId Long memberId,
      @Valid @RequestBody PromptRequest request
  ) {
    promptService.extractFlight(memberId,
        request);
  }

  @PostMapping("/integration")
  public void extractIntegration(
      @MemberId Long memberId,
      @Valid @RequestBody PromptRequest request
  ) {
    IntegrationResponse response = promptService.extractIntegration(memberId, request);

    String pythonUrl = "http://localhost:8000/integration";

  }

}