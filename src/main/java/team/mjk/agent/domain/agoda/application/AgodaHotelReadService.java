package team.mjk.agent.domain.agoda.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.mjk.agent.domain.agoda.dto.request.AgodaHotelRequest;
import team.mjk.agent.domain.agoda.dto.response.AgodaHotelResponse;
import team.mjk.agent.domain.agoda.presentation.exception.HotelInfoNotFoundException;
import team.mjk.agent.domain.hotel.dto.AgodaHotel;
import team.mjk.agent.domain.hotel.dto.AgodaHotelList;
import team.mjk.agent.domain.hotel.dto.Hotel;
import team.mjk.agent.domain.hotel.dto.HotelList;
import team.mjk.agent.domain.prompt.dto.request.PromptRequest;
import team.mjk.agent.domain.prompt.application.PromptService;

@RequiredArgsConstructor
@Service
public class AgodaHotelReadService {

    private final AgodaCityService cityService;
    private final AgodaHotelApiClient agodaHotelApiClient;
    private final PromptService promptService;

    public AgodaHotelResponse getHotelsFromPrompt(Long memberId, String prompt) {
        AgodaHotelList agodaHotelList = promptService.extractAgodaHotelInfo(memberId, new PromptRequest(prompt));

        AgodaHotel hotel = validateHotelInfo(agodaHotelList);

        int cityId;
        try {
            cityId = Integer.parseInt(cityService.getCityId(hotel.destination()));
        } catch (Exception e) {
            throw new HotelInfoNotFoundException();
        }

        AgodaHotelRequest request = AgodaHotelRequest.of(
                hotel,
                cityId
        );

        return agodaHotelApiClient.fetchHotels(request);
    }

    private AgodaHotel validateHotelInfo(AgodaHotelList hotelList) {
        if (hotelList == null || hotelList.agodaHotelList().isEmpty()) {
            throw new HotelInfoNotFoundException();
        }
        return hotelList.agodaHotelList().get(0);
    }

}
