package team.mjk.agent.domain.member.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.mjk.agent.domain.member.application.MemberService;
import team.mjk.agent.domain.member.dto.request.MemberInfoSaveRequest;
import team.mjk.agent.domain.member.dto.request.MemberSaveRequest;
import team.mjk.agent.domain.member.dto.response.MemberInfoSaveResponse;
import team.mjk.agent.domain.member.dto.response.MemberSaveResponse;
import team.mjk.agent.global.annotation.MemberId;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberSaveResponse> signUp(
            @Valid @RequestBody MemberSaveRequest request
    ) {
        MemberSaveResponse response = memberService.signUp(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/sensitive-member-info")
    public ResponseEntity<MemberInfoSaveResponse> saveMemberInfo(
            @MemberId Long memberId,
            @RequestBody MemberInfoSaveRequest request
    ) {
        MemberInfoSaveResponse response = memberService.saveMemberInfo(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
