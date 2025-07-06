package team.mjk.agent.domain.company.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.mjk.agent.domain.company.application.CompanyService;
import team.mjk.agent.domain.invitation.domain.Invitation;

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

}