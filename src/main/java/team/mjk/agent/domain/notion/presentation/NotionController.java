package team.mjk.agent.domain.notion.presentation;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.mjk.agent.domain.notion.application.NotionService;
import team.mjk.agent.domain.notion.dto.request.NotionTokenRequest;
import team.mjk.agent.domain.notion.dto.request.NotionTokenUpdateRequest;
import team.mjk.agent.global.annotation.MemberId;

@RequiredArgsConstructor
@RequestMapping("/notions")
@RestController
public class NotionController {

  private final NotionService notionService;

  @PostMapping
  public ResponseEntity<Long> saveNotion(
      @MemberId Long memberId,
      @Valid @RequestBody NotionTokenRequest request
  ) {
    Long response = notionService.save(memberId, request);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PatchMapping
  public ResponseEntity<Long> updateNotion(
      @MemberId Long memberId,
      @Valid @RequestBody NotionTokenUpdateRequest request
  ) {
    Long response = notionService.update(memberId, request);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

}