package team.mjk.agent.domain.company.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.mjk.agent.domain.company.application.CompanyService;
import team.mjk.agent.domain.company.dto.request.CompanyInvitationCodeRequest;
import team.mjk.agent.domain.company.dto.request.CompanySaveRequest;
import team.mjk.agent.domain.invitation.domain.Invitation;
import team.mjk.agent.global.annotation.MemberId;

@RequiredArgsConstructor
@RequestMapping("/companies")
@RestController
public class CompanyController {

  private final CompanyService companyService;

  @GetMapping("/{companyId}/invitation-code")
  public ResponseEntity<String> createInvitationCode(@PathVariable Long companyId) {
    Invitation invitationCode = companyService.createInvitationCode(companyId);

    return new ResponseEntity<>(invitationCode.getCode(), HttpStatus.CREATED);
  }

  @PostMapping
  public ResponseEntity<Long> createCompany(
      @MemberId Long memberId,
      @RequestBody CompanySaveRequest request
  ) {
    Long companyId = companyService.create(request, memberId);

    return new ResponseEntity<>(companyId, HttpStatus.CREATED);
  }

  @PostMapping("/join")
  public ResponseEntity<String> joinCompany(
      @MemberId Long memberId,
      @RequestBody CompanyInvitationCodeRequest request
  ) {
    String companyName = companyService.join(memberId,request);

    return new ResponseEntity<>(companyName, HttpStatus.OK);
  }



}