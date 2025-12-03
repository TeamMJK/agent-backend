package team.mjk.agent.domain.company.presentation.query;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.mjk.agent.domain.company.application.dto.response.CompanyInfoResponse;
import team.mjk.agent.domain.company.application.dto.response.CompanyMemberListResponse;
import team.mjk.agent.domain.company.application.query.CompanyQueryService;
import team.mjk.agent.global.annotation.MemberId;

@RequiredArgsConstructor
@RequestMapping("/companies")
@RestController
public class CompanyQueryController {

    private final CompanyQueryService companyQueryService;

    @GetMapping
    public ResponseEntity<CompanyInfoResponse> getCompany(
            @MemberId Long memberId
    ) {
        CompanyInfoResponse companyName = companyQueryService.getCompanyInfo(memberId);

        return new ResponseEntity<>(companyName, HttpStatus.OK);
    }

    @GetMapping("/members")
    public ResponseEntity<CompanyMemberListResponse> getMembersCompany(
            @MemberId Long memberId
    ) {
        CompanyMemberListResponse response = companyQueryService.getMembersInfo(memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
