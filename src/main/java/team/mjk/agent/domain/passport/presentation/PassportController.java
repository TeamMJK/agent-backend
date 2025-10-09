package team.mjk.agent.domain.passport.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.mjk.agent.domain.passport.application.PassportService;
import team.mjk.agent.domain.passport.application.dto.response.PassportInfoResponse;
import team.mjk.agent.domain.passport.application.dto.response.PassportSaveResponse;
import team.mjk.agent.domain.passport.application.dto.response.PassportUpdateResponse;
import team.mjk.agent.domain.passport.presentation.request.PassportSaveRequest;
import team.mjk.agent.domain.passport.presentation.request.PassportUpdateRequest;
import team.mjk.agent.global.annotation.MemberId;

@RequiredArgsConstructor
@RequestMapping("/passports")
@RestController
public class PassportController {

    private final PassportService passportService;

    @PostMapping
    public ResponseEntity<PassportSaveResponse> save(
            @MemberId Long memberId,
            @Valid @RequestBody PassportSaveRequest request
    ) {
        PassportSaveResponse response = passportService.savePassport(request.toServiceRequest(memberId));
        return ResponseEntity.status(201).body(response);
    }

    @PatchMapping
    public ResponseEntity<PassportUpdateResponse> update(
        @MemberId Long memberId,
        @Valid @RequestBody PassportUpdateRequest request
    ) {
        PassportUpdateResponse response = passportService.updatePassport(request.toServiceRequest(memberId));
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping
    public ResponseEntity<PassportInfoResponse> getPassportInfo(
            @MemberId Long memberId
    ) {
        PassportInfoResponse response = passportService.getPassportInfo(memberId);
        return ResponseEntity.status(200).body(response);
    }

}
