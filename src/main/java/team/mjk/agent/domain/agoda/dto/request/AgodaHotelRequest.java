package team.mjk.agent.domain.agoda.dto.request;

import lombok.Builder;

@Builder
public record AgodaHotelRequest(

        Criteria criteria

) {
        public static AgodaHotelRequest of(String checkInDate, String checkOutDate, int budget, int cityId) {
        return AgodaHotelRequest.builder()
                .criteria(Criteria.builder()
                        .additional(Additional.builder()
                                .currency("KRW")
                                .dailyRate(DailyRate.builder().minimum(1).maximum(budget).build())
                                .discountOnly(false)
                                .language("ko-kr")
                                .maxResult(10)
                                .minimumReviewScore(0)
                                .minimumStarRating(0)
                                .occupancy(Occupancy.builder().numberOfAdult(2).numberOfChildren(1).build())
                                .sortBy("PriceAsc")
                                .build())
                        .checkInDate(checkInDate)
                        .checkOutDate(checkOutDate)
                        .cityId(cityId)
                        .build())
                .build();
    }
}
