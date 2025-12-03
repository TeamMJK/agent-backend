package team.mjk.agent.domain.member.presentation.command;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.mjk.agent.domain.member.application.command.MemberCommandService;
import team.mjk.agent.domain.member.application.dto.response.MemberSaveInfoResponse;
import team.mjk.agent.domain.member.application.dto.response.MemberSaveResponse;
import team.mjk.agent.domain.member.application.dto.response.MemberUpdateInfoResponse;
import team.mjk.agent.domain.member.presentation.request.MemberSaveInfoRequest;
import team.mjk.agent.domain.member.presentation.request.MemberSaveRequest;
import team.mjk.agent.domain.member.presentation.request.MemberUpdateInfoRequest;
import team.mjk.agent.global.annotation.MemberId;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberCommandController implements MemberCommandDocsController {

    private final MemberCommandService memberCommandService;

    @PostMapping
    public ResponseEntity<MemberSaveResponse> signUp(
            @Valid @RequestBody MemberSaveRequest request
    ) {
        MemberSaveResponse response = memberCommandService.signUp(request.toServiceRequest());
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/sensitive-member-info")
    public ResponseEntity<MemberSaveInfoResponse> saveMemberInfo(
            @MemberId Long memberId,
            @Valid @RequestBody MemberSaveInfoRequest request
    ) {
        MemberSaveInfoResponse response = memberCommandService.saveMemberInfo(request.toServiceRequest(memberId));
        return ResponseEntity.status(201).body(response);
    }

    @PatchMapping("/me")
    public ResponseEntity<MemberUpdateInfoResponse> updateMemberInfo(
            @MemberId Long memberId,
            @Valid @RequestBody MemberUpdateInfoRequest request
    ) {
        MemberUpdateInfoResponse response = memberCommandService.updateMemberInfo(request.toServiceRequest(memberId));
        return ResponseEntity.status(204).body(response);
    }

    @DeleteMapping
    public ResponseEntity<Long> deleteMember(
            @MemberId Long memberId
    ) {
        Long deleteMemberId = memberCommandService.delete(memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(deleteMemberId);
    }

}
