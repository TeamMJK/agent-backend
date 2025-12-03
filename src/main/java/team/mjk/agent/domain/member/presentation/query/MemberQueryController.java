package team.mjk.agent.domain.member.presentation.query;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.mjk.agent.domain.member.application.dto.response.MemberInfoGetResponse;
import team.mjk.agent.domain.member.application.query.MemberQueryService;
import team.mjk.agent.global.annotation.MemberId;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberQueryController implements MemberQueryDocsController {

    private final MemberQueryService memberQueryService;

    @GetMapping("/me")
    public ResponseEntity<MemberInfoGetResponse> getMemberInfo(@MemberId Long memberId) {
        MemberInfoGetResponse response = memberQueryService.getMemberInfo(memberId);
        return ResponseEntity.ok(response);
    }

}
