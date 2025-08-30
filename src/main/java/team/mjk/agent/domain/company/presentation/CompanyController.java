package team.mjk.agent.domain.company.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.mjk.agent.domain.company.application.CompanyService;
import team.mjk.agent.domain.company.dto.request.CompanyInvitationCodeRequest;
import team.mjk.agent.domain.company.dto.request.CompanyInvitationEmailRequest;
import team.mjk.agent.domain.company.dto.request.CompanySaveRequest;
import team.mjk.agent.domain.company.dto.request.CompanyUpdateRequest;
import team.mjk.agent.domain.company.dto.response.CompanyInfoResponse;
import team.mjk.agent.domain.company.dto.response.CompanyInvitationEmailResponse;
import team.mjk.agent.domain.company.dto.response.CompanyJoinResponse;
import team.mjk.agent.global.annotation.MemberId;

@RequiredArgsConstructor
@RequestMapping("/companies")
@RestController
public class CompanyController implements CompanyDocsController {

  private final CompanyService companyService;

  @PostMapping("/invitation/send")
  public ResponseEntity<CompanyInvitationEmailResponse> sendInvitationCode(
          @MemberId Long memberId,
          @Valid @RequestBody CompanyInvitationEmailRequest request
  ) {
    CompanyInvitationEmailResponse response =
            companyService.createInvitationCodeAndSendEmail(memberId, request);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<Long> createCompany(
      @MemberId Long memberId,
      @Valid @RequestBody CompanySaveRequest request
  ) {
    Long companyId = companyService.create(request, memberId);

    return new ResponseEntity<>(companyId, HttpStatus.CREATED);
  }

  @PostMapping("/join")
  public ResponseEntity<CompanyJoinResponse> joinCompany(
      @MemberId Long memberId,
      @RequestBody CompanyInvitationCodeRequest request
  ) {
    CompanyJoinResponse response = companyService.join(memberId,request);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<CompanyInfoResponse> getCompany(
      @MemberId Long memberId
  ) {
    CompanyInfoResponse companyName = companyService.getCompanyInfo(memberId);

    return new ResponseEntity<>(companyName, HttpStatus.OK);
  }

  @PatchMapping
  public ResponseEntity<Long> updateCompany(
      @MemberId Long memberId,
      @Valid @RequestBody CompanyUpdateRequest request
  ) {
    Long companyId = companyService.update(memberId,request);
    return new ResponseEntity<>(companyId,HttpStatus.OK);
  }

  @DeleteMapping
  public ResponseEntity<Long> deleteCompany(
      @MemberId Long memberId
  ) {
    Long companyId = companyService.delete(memberId);
    return new ResponseEntity<>(companyId,HttpStatus.OK);
  }

}