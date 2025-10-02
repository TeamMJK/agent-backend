package team.mjk.agent.domain.agoda.dto.response;

import lombok.Builder;

@Builder
public record HotelResult(

        double crossedOutRate,
        String currency,
        double dailyRate,
        int discountPercentage,
        boolean freeWifi,
        long hotelId,
        String hotelName,
        String imageURL,
        boolean includeBreakfast,
        String landingURL,
        double reviewScore,
        double starRating

) {
}
