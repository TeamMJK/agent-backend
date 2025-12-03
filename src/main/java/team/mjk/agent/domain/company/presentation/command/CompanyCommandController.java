package team.mjk.agent.domain.company.presentation.command;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.mjk.agent.domain.company.application.command.CompanyCommandService;
import team.mjk.agent.domain.company.application.dto.response.CompanyInvitationEmailResponse;
import team.mjk.agent.domain.company.application.dto.response.CompanyJoinResponse;
import team.mjk.agent.domain.company.application.dto.response.CompanySaveResponse;
import team.mjk.agent.domain.company.application.dto.response.CompanyUpdateResponse;
import team.mjk.agent.domain.company.presentation.request.CompanyInvitationCodeRequest;
import team.mjk.agent.domain.company.presentation.request.CompanyInvitationEmailRequest;
import team.mjk.agent.domain.company.presentation.request.CompanySaveRequest;
import team.mjk.agent.domain.company.presentation.request.CompanyUpdateRequest;
import team.mjk.agent.global.annotation.MemberId;

@RequiredArgsConstructor
@RequestMapping("/companies")
@RestController
public class CompanyCommandController implements CompanyCommandDocsController {

    private final CompanyCommandService companyCommandService;

    @PostMapping
    public ResponseEntity<CompanySaveResponse> createCompany(
            @MemberId Long memberId,
            @Valid @RequestBody CompanySaveRequest request
    ) {
        CompanySaveResponse response = companyCommandService.saveCompany(request.toServiceRequest(memberId));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/invitation/send")
    public ResponseEntity<CompanyInvitationEmailResponse> sendInvitationCode(
            @MemberId Long memberId,
            @Valid @RequestBody CompanyInvitationEmailRequest request
    ) {
        CompanyInvitationEmailResponse response =
                companyCommandService.createInvitationCodeAndSendEmail(request.toServiceRequest(memberId));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/join")
    public ResponseEntity<CompanyJoinResponse> joinCompany(
            @MemberId Long memberId,
            @RequestBody CompanyInvitationCodeRequest request
    ) {
        CompanyJoinResponse response = companyCommandService.join(request.toServiceRequest(memberId));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<CompanyUpdateResponse> updateCompany(
            @MemberId Long memberId,
            @Valid @RequestBody CompanyUpdateRequest request
    ) {
        CompanyUpdateResponse response = companyCommandService.update(request.toServiceRequest(memberId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Long> deleteCompany(
            @MemberId Long memberId
    ) {
        Long companyId = companyCommandService.delete(memberId);
        return new ResponseEntity<>(companyId, HttpStatus.NO_CONTENT);
    }

}
