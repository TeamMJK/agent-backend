package team.mjk.agent.domain.businessTrip.presentation.query;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.mjk.agent.domain.businessTrip.application.query.BusinessTripQueryService;

@RequiredArgsConstructor
@RequestMapping("/business-trips")
@RestController
public class BusinessTripQueryController {

    private final BusinessTripQueryService businessTripQueryService;



}
