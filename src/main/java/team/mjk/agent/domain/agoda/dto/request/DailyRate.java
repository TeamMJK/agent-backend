package team.mjk.agent.domain.agoda.dto.request;

import java.util.Optional;
import lombok.Builder;

@Builder
public record DailyRate(

    Integer minimum,
    Integer maximum

) {

}
