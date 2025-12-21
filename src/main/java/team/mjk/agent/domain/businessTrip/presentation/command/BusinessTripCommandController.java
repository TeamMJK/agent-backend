package team.mjk.agent.domain.businessTrip.presentation.command;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.mjk.agent.domain.businessTrip.application.command.BusinessTripCommandService;

@RequiredArgsConstructor
@RequestMapping("/business-trips")
@RestController
public class BusinessTripCommandController {

    private final BusinessTripCommandService businessTripCommandService;

}
