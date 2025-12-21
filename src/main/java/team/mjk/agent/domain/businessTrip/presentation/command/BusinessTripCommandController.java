package team.mjk.agent.domain.businessTrip.presentation.command;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.mjk.agent.domain.businessTrip.application.command.BusinessTripCommandService;
import team.mjk.agent.domain.businessTrip.application.dto.response.BusinessTripSaveResponse;
import team.mjk.agent.domain.businessTrip.application.dto.response.BusinessTripUpdateResponse;
import team.mjk.agent.domain.businessTrip.presentation.request.BusinessTripSaveRequest;
import team.mjk.agent.domain.businessTrip.presentation.request.BusinessTripUpdateRequest;
import team.mjk.agent.domain.companyworkspace.domain.Workspace;
import team.mjk.agent.global.annotation.MemberId;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/business-trips")
@RestController
public class BusinessTripCommandController {

    private final BusinessTripCommandService businessTripCommandService;

    //사용자가 WorkSpace 상관없이 MJK 워크스페이스에 저장할 때
    @PostMapping
    public ResponseEntity<BusinessTripSaveResponse> saveBusinessTrip(
            @MemberId Long memberId,
            @RequestBody BusinessTripSaveRequest request
    ) {
        BusinessTripSaveResponse response = businessTripCommandService.save(request.toServiceRequest(memberId));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //사용자가 출장 정보를 생성할 때
    @PostMapping("/mcp")
    public ResponseEntity<List<Workspace>> saveBusinessTripMcp(
            @MemberId Long memberId,
            @RequestBody BusinessTripSaveRequest request
    ) {
        List<Workspace> workspace = businessTripCommandService.saveMcp(request.toServiceRequest(memberId));
        return new ResponseEntity<>(workspace, HttpStatus.CREATED);
    }

    @PatchMapping("/{business-trip-id}")
    public ResponseEntity<BusinessTripUpdateResponse> updateBusinessTrip(
            @MemberId Long memberId,
            @PathVariable("business-trip-id") Long businessTripId,
            @RequestBody BusinessTripUpdateRequest request
    ) {
        BusinessTripUpdateResponse response = businessTripCommandService.update(request.toServiceRequest(memberId, businessTripId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(("/{business-trip-id}"))
    public ResponseEntity<Void> deleteBusinessTrip(
            @MemberId Long memberId,
            @PathVariable("business-trip-id") Long businessTripId
    ) {
        businessTripCommandService.delete(memberId, businessTripId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
