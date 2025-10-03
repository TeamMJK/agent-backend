package team.mjk.agent.domain.agoda.application;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.mjk.agent.domain.agoda.dto.request.AgodaHotelRequest;
import team.mjk.agent.domain.agoda.dto.response.AgodaHotelResponse;
import team.mjk.agent.domain.agoda.dto.response.HotelResult;
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

    public List<AgodaHotelResponse> getHotelsFromPrompt(Long memberId, String prompt) {
        AgodaHotelList agodaHotelList = promptService.extractAgodaHotelInfo(memberId, new PromptRequest(prompt));

        if (agodaHotelList == null || agodaHotelList.agodaHotelList().isEmpty()) {
            throw new HotelInfoNotFoundException();
        }

        List<AgodaHotelResponse> responses = new ArrayList<>();

        for (AgodaHotel hotel : agodaHotelList.agodaHotelList()) {
            int cityId;
            System.out.println(hotel.destination());
            try {
                cityId = Integer.parseInt(cityService.getCityId(hotel.destination()));
            } catch (Exception e) {
                throw new HotelInfoNotFoundException();
            }

            AgodaHotelRequest request = AgodaHotelRequest.of(hotel, cityId);
            AgodaHotelResponse agodaHotelResponse = agodaHotelApiClient.fetchHotels(request);

            List<HotelResult> agodaHotelInfoResponse = agodaHotelResponse.results().stream()
                .map(r -> HotelResult.builder()
                    .crossedOutRate(r.crossedOutRate())
                    .currency(r.currency())
                    .dailyRate(r.dailyRate())
                    .discountPercentage(r.discountPercentage())
                    .freeWifi(r.freeWifi())
                    .hotelId(r.hotelId())
                    .hotelName(r.hotelName())
                    .imageURL(r.imageURL())
                    .includeBreakfast(r.includeBreakfast())
                    .landingURL(r.landingURL())
                    .reviewScore(r.reviewScore())
                    .starRating(r.starRating())
                    .agodaHotelInfo(promptService.extractAgodaHotelInfo(r))
                    .build()
                )
                .toList();

            AgodaHotelResponse response = AgodaHotelResponse.builder()
                .results(agodaHotelInfoResponse)
                .destination(hotel.destination())
                .build();

            responses.add(response);
        }

        return responses;
    }

}