package team.mjk.agent.domain.agoda.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.mjk.agent.domain.agoda.dto.response.AgodaHotelResponse;

@RequiredArgsConstructor
@Service
public class AgodaService {

    private final AgodaHotelReadService agodaHotelReadService;

    public AgodaHotelResponse getHotels(Long memberId, String prompt) {
        return agodaHotelReadService.getHotelsFromPrompt(memberId, prompt);
    }

}
