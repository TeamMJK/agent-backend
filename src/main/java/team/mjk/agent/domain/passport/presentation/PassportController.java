package team.mjk.agent.domain.passport.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.mjk.agent.domain.passport.application.PassportService;
import team.mjk.agent.domain.passport.dto.request.PassportInfoSaveRequest;
import team.mjk.agent.domain.passport.dto.response.PassportInfoSaveResponse;
import team.mjk.agent.global.annotation.MemberId;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class PassportController implements PassportDocsController {

  private final PassportService passportService;

  @PostMapping("/sensitive-passport-info")
  public ResponseEntity<PassportInfoSaveResponse> savePassportInfo(
      @MemberId Long memberId,
      @RequestBody PassportInfoSaveRequest request
  ) {
    PassportInfoSaveResponse response = passportService.savePassportInfo(memberId, request);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

}