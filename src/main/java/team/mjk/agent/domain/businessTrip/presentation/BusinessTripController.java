package team.mjk.agent.domain.businessTrip.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.mjk.agent.domain.businessTrip.application.BusinessTripService;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripSaveRequest;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripUpdateRequest;
import team.mjk.agent.domain.businessTrip.dto.response.BusinessTripGetAllResponse;
import team.mjk.agent.domain.businessTrip.dto.response.BusinessTripGetResponse;
import team.mjk.agent.domain.businessTrip.dto.response.BusinessTripSaveResponse;
import team.mjk.agent.domain.businessTrip.dto.response.BusinessTripUpdateResponse;
import team.mjk.agent.global.annotation.MemberId;

@RequiredArgsConstructor
@RequestMapping("/business-trips")
@RestController
public class BusinessTripController implements BusinessTripDocsController{

  private final BusinessTripService businessTripService;

  @PostMapping
  public ResponseEntity<BusinessTripSaveResponse> saveBusinessTrip(
      @MemberId Long memberId,
      @RequestBody BusinessTripSaveRequest request
  ) {
    BusinessTripSaveResponse response = businessTripService.save(memberId, request);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping("/{business-trip-id}")
  public ResponseEntity<BusinessTripGetResponse> getBusinessTrip(
      @MemberId Long memberId,
      @PathVariable("business-trip-id") Long businessTripId

  ) {
    BusinessTripGetResponse response = businessTripService.getBusinessTrip(memberId, businessTripId);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PatchMapping("/{business-trip-id}")
  public ResponseEntity<BusinessTripUpdateResponse> updateBusinessTrip(
      @MemberId Long memberId,
      @PathVariable("business-trip-id") Long businessTripId,
      @RequestBody BusinessTripUpdateRequest request
  ) {
    BusinessTripUpdateResponse response = businessTripService.update(memberId, businessTripId, request);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<BusinessTripGetAllResponse> getAllBusinessTrip(@MemberId Long memberId) {
    BusinessTripGetAllResponse response = businessTripService.getAllBusinessTrip(memberId);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

}