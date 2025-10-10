package team.mjk.agent.domain.agoda.presentation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.mjk.agent.domain.agoda.application.AgodaService;
import team.mjk.agent.domain.agoda.dto.response.AgodaHotelResponse;
import team.mjk.agent.domain.prompt.dto.request.PromptRequest;
import team.mjk.agent.global.annotation.MemberId;

@RequiredArgsConstructor
@RequestMapping("/agodas")
@RestController
public class AgodaController implements AgodaDocsController {

    private final AgodaService agodaService;

    @PostMapping("/search")
    public List<AgodaHotelResponse> searchHotels(
            @MemberId Long memberId,
            @RequestBody PromptRequest request
    ) {
        return agodaService.getHotels(memberId, request.prompt());
    }

}