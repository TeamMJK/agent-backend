package team.mjk.agent.domain.vnc.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.mjk.agent.domain.vnc.application.VncService;
import team.mjk.agent.domain.vnc.dto.VncResponseList;
import team.mjk.agent.domain.vnc.dto.VncSessionIdRequest;
import team.mjk.agent.global.annotation.MemberId;

@RequiredArgsConstructor
@RequestMapping("/vnc")
@RestController
public class VncController {

  private final VncService vncService;

  @GetMapping
  public ResponseEntity<VncResponseList> getStatus(@MemberId Long memberId) {
    VncResponseList vncResponseList = vncService.getVncList(memberId);
    return ResponseEntity.ok(vncResponseList);
  }

  @PostMapping("/pause")
  public ResponseEntity<VncResponseList> pause(
          @MemberId Long memberId,
          @RequestBody VncSessionIdRequest request
  ) {
    VncResponseList vncResponseList = vncService.pause(memberId, request);
    return ResponseEntity.ok(vncResponseList);
  }

  @PostMapping("/unpause")
  public ResponseEntity<VncResponseList> unpause(
          @MemberId Long memberId,
          @RequestBody VncSessionIdRequest request
  ) {
    VncResponseList vncResponseList = vncService.unpause(memberId, request);
    return ResponseEntity.ok(vncResponseList);
  }
}
