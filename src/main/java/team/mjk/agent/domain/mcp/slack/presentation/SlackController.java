package team.mjk.agent.domain.mcp.slack.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.mjk.agent.domain.mcp.slack.application.SlackService;
import team.mjk.agent.domain.mcp.slack.dto.request.SlackSaveRequest;
import team.mjk.agent.domain.mcp.slack.dto.request.SlackUpdateRequest;
import team.mjk.agent.global.annotation.MemberId;

@RequiredArgsConstructor
@RequestMapping("/slacks")
@RestController
public class SlackController {

  private final SlackService slackService;

  @PostMapping
  public ResponseEntity<Long> saveSlack(
      @MemberId Long memberId,
      @Valid @RequestBody SlackSaveRequest request
  ) {
    Long response = slackService.save(memberId, request);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PatchMapping
  public ResponseEntity<Long> updateSlack(
      @MemberId Long memberId,
      @Valid @RequestBody SlackUpdateRequest request
  ) {
    Long response = slackService.update(memberId, request);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping
  public ResponseEntity<Long> deleteSlack(
      @MemberId Long memberId
  ) {
    Long response = slackService.delete(memberId);
    return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
  }

}