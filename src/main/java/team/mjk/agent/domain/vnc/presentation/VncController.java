package team.mjk.agent.domain.vnc.presentation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.mjk.agent.domain.vnc.application.VncCacheService;
import team.mjk.agent.domain.vnc.application.VncService;
import team.mjk.agent.domain.vnc.dto.VncResponse;
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

  @GetMapping("/pause")
  public ResponseEntity<List<VncResponse>> pause(
      @MemberId Long memberId,
      @RequestBody VncSessionIdRequest request
  ) {
    List<VncResponse> vncResponse = vncService.pause(memberId,request);

    return new ResponseEntity<>(vncResponse, HttpStatus.OK);
  }

  @GetMapping("/unpause")
  public ResponseEntity<List<VncResponse>> unpause(
      @MemberId Long memberId,
      @RequestBody VncSessionIdRequest request
  ) {
    List<VncResponse> vncResponse = vncService.unpause(memberId,request);

    return new ResponseEntity<>(vncResponse, HttpStatus.OK);
  }

}