package team.mjk.agent.domain.agoda.dto.response;

import lombok.Builder;
import lombok.Singular;

import java.util.List;

@Builder
public record AgodaHotelResponse(

        @Singular
        List<HotelResult> results,
        String destination

) {
}
