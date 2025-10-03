package team.mjk.agent.domain.agoda.dto.request;

import java.util.Collections;
import java.util.Optional;
import lombok.Builder;
import team.mjk.agent.domain.hotel.dto.AgodaHotel;

@Builder
public record AgodaHotelRequest(

    Criteria criteria

) {

  public static AgodaHotelRequest of(
      AgodaHotel agodaHotel,
      int cityId
  ) {
    // 기본값 설정
    int defaultBudget = 1000000;
    int defaultMinimum = 1;
    int defaultAdults = 2;
    int defaultChildren = 0;
    AgodaSort defaultSort = AgodaSort.RECOMMENDED;
    int defaultMaxResult = 10;
    double defaultMinimumReviewScore = 0.0;
    double defaultMinimumStarRating = 0.0;

    DailyRate dailyRate = agodaHotel.dailyRate() != null
        ? DailyRate.builder()
        .minimum(agodaHotel.dailyRate().minimum() != null ? agodaHotel.dailyRate().minimum()
            : defaultMinimum)
        .maximum(agodaHotel.dailyRate().maximum() != null ? agodaHotel.dailyRate().maximum()
            : defaultBudget)
        .build()
        : DailyRate.builder()
            .minimum(defaultMinimum)
            .maximum(defaultBudget)
            .build();

    Occupancy occupancy = agodaHotel.occupancy() != null
        ? Occupancy.builder()
        .numberOfAdult(
            agodaHotel.occupancy().numberOfAdult() != null ? agodaHotel.occupancy().numberOfAdult()
                : defaultAdults)
        .numberOfChildren(agodaHotel.occupancy().numberOfChildren() != null ? agodaHotel.occupancy()
            .numberOfChildren() : defaultChildren)
        .childrenAges(
            agodaHotel.occupancy().childrenAges() != null ? agodaHotel.occupancy().childrenAges()
                : Collections.emptyList())
        .build()
        : Occupancy.builder()
            .numberOfAdult(defaultAdults)
            .numberOfChildren(defaultChildren)
            .childrenAges(Collections.emptyList())
            .build();

    String sortBy =
        agodaHotel.agodaSort() != null ? agodaHotel.agodaSort().getValue() : defaultSort.getValue();

    System.out.println(agodaHotel.departure_date());
    System.out.println(agodaHotel.arrival_date());
    return AgodaHotelRequest.builder()
        .criteria(Criteria.builder()
            .additional(Additional.builder()
                .currency("KRW")
                .dailyRate(dailyRate)
                .discountOnly(agodaHotel.discountOnly())
                .language("ko-kr")
                .maxResult(
                    agodaHotel.maxResult() != null ? agodaHotel.maxResult() : defaultMaxResult
                )
                .minimumReviewScore(
                    agodaHotel.minimumReviewScore() != null ? agodaHotel.minimumReviewScore()
                        : defaultMinimumReviewScore
                )
                .minimumStarRating(
                    agodaHotel.minimumStarRating() != null ? agodaHotel.minimumStarRating()
                        : defaultMinimumStarRating
                )
                .occupancy(occupancy)
                .sortBy(sortBy)
                .build())
            .checkInDate(agodaHotel.departure_date())
            .checkOutDate(agodaHotel.arrival_date())
            .cityId(cityId)
            .build())
        .build();
  }

}