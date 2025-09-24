package team.mjk.agent.domain.vnc.presentation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.mjk.agent.domain.vnc.application.VncService;
import team.mjk.agent.domain.vnc.dto.VncResponse;
import team.mjk.agent.domain.vnc.dto.VncResponseList;
import team.mjk.agent.global.annotation.MemberId;

@RequiredArgsConstructor
@RequestMapping("/vnc")
@RestController
public class VncController {

  private final VncService vncService;

  @GetMapping
  public ResponseEntity<List<VncResponse>> getStatus(@MemberId Long memberId) {
    List<VncResponse> vncResponse = vncService.getVncList(memberId);

    return new ResponseEntity<>(vncResponse, HttpStatus.OK);
  }

}