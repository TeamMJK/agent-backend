package team.mjk.agent.domain.prompt.presentation;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.mjk.agent.domain.prompt.application.PromptService;
import team.mjk.agent.domain.prompt.dto.request.PromptRequest;
import team.mjk.agent.domain.prompt.dto.response.HotelAndMemberInfoResponse;
import team.mjk.agent.global.annotation.MemberId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/prompts")
public class PromptController {

  private final PromptService promptService;

  @PostMapping
  public ResponseEntity<HotelAndMemberInfoResponse> extractPrompt(
      @MemberId Long memberId,
      @Valid @RequestBody PromptRequest request
  ) {
    HotelAndMemberInfoResponse response = promptService.extract(memberId, request);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

}