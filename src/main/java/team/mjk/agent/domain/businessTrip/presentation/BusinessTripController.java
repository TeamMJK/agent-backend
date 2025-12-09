package team.mjk.agent.domain.businessTrip.presentation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.mjk.agent.domain.businessTrip.application.BusinessTripService;
import team.mjk.agent.domain.businessTrip.presentation.request.BusinessTripSaveRequest;
import team.mjk.agent.domain.businessTrip.presentation.request.BusinessTripUpdateRequest;
import team.mjk.agent.domain.businessTrip.application.dto.response.BusinessTripGetAllResponse;
import team.mjk.agent.domain.businessTrip.application.dto.response.BusinessTripGetResponse;
import team.mjk.agent.domain.businessTrip.application.dto.response.BusinessTripSaveResponse;
import team.mjk.agent.domain.businessTrip.application.dto.response.BusinessTripUpdateResponse;
import team.mjk.agent.domain.companyworkspace.domain.Workspace;
import team.mjk.agent.global.annotation.MemberId;

@RequiredArgsConstructor
@RequestMapping("/business-trips")
@RestController
public class BusinessTripController implements BusinessTripDocsController {

  private final BusinessTripService businessTripService;

  //사용자가 WorkSpace 상관없이 MJK 워크스페이스에 저장할 때
  @PostMapping
  public ResponseEntity<BusinessTripSaveResponse> saveBusinessTrip(
      @MemberId Long memberId,
      @RequestBody BusinessTripSaveRequest request
  ) {
    BusinessTripSaveResponse response = businessTripService.save(request.toServiceRequest(memberId));
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  //사용자가 출장 정보를 생성할 때
  @PostMapping("/mcp")
  public ResponseEntity<List<Workspace>> saveBusinessTripMcp(
      @MemberId Long memberId,
      @RequestBody BusinessTripSaveRequest request
  ) {
    List<Workspace> workspace = businessTripService.saveMcp(request.toServiceRequest(memberId));
    return new ResponseEntity<>(workspace, HttpStatus.CREATED);
  }

  @GetMapping("/{business-trip-id}")
  public ResponseEntity<BusinessTripGetResponse> getBusinessTrip(
      @MemberId Long memberId,
      @PathVariable("business-trip-id") Long businessTripId

  ) {
    BusinessTripGetResponse response = businessTripService.getBusinessTrip(memberId,
        businessTripId);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PatchMapping("/{business-trip-id}")
  public ResponseEntity<BusinessTripUpdateResponse> updateBusinessTrip(
      @MemberId Long memberId,
      @PathVariable("business-trip-id") Long businessTripId,
      @RequestBody BusinessTripUpdateRequest request
  ) {
    BusinessTripUpdateResponse response = businessTripService.update(memberId, businessTripId,
        request);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<BusinessTripGetAllResponse> getAllBusinessTrip(@MemberId Long memberId) {
    BusinessTripGetAllResponse response = businessTripService.getAllBusinessTrip(memberId);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping(("/{business-trip-id}"))
  public ResponseEntity<Void> deleteBusinessTrip(
      @MemberId Long memberId,
      @PathVariable("business-trip-id") Long businessTripId
  ) {
    businessTripService.delete(memberId, businessTripId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}