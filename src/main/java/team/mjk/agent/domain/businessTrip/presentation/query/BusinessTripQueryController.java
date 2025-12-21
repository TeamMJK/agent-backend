package team.mjk.agent.domain.businessTrip.presentation.query;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.mjk.agent.domain.businessTrip.application.dto.response.BusinessTripGetAllResponse;
import team.mjk.agent.domain.businessTrip.application.dto.response.BusinessTripGetResponse;
import team.mjk.agent.domain.businessTrip.application.query.BusinessTripQueryService;
import team.mjk.agent.global.annotation.MemberId;

@RequiredArgsConstructor
@RequestMapping("/business-trips")
@RestController
public class BusinessTripQueryController {

    private final BusinessTripQueryService businessTripQueryService;

    @GetMapping("/{business-trip-id}")
    public ResponseEntity<BusinessTripGetResponse> getBusinessTrip(
            @MemberId Long memberId,
            @PathVariable("business-trip-id") Long businessTripId
    ) {
        BusinessTripGetResponse response = businessTripQueryService.getBusinessTrip(memberId, businessTripId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<BusinessTripGetAllResponse> getAllBusinessTrip(@MemberId Long memberId) {
        BusinessTripGetAllResponse response = businessTripQueryService.getAllBusinessTrip(memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
