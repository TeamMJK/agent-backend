package team.mjk.agent.domain.hotel.dto;

import jakarta.annotation.Nullable;
import java.util.Optional;
import team.mjk.agent.domain.agoda.dto.request.AgodaSort;
import team.mjk.agent.domain.agoda.dto.request.DailyRate;
import team.mjk.agent.domain.agoda.dto.request.Occupancy;

public record AgodaHotel(
    String departure_date,
    String arrival_date,
    String destination,
    boolean discountOnly,
    Integer maxResult,
    Double minimumStarRating,
    Double minimumReviewScore,

    @Nullable
    AgodaSort agodaSort,

    @Nullable
    DailyRate dailyRate,

    @Nullable
    Occupancy occupancy
) {

}
