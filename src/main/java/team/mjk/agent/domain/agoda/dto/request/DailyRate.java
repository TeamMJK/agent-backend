package team.mjk.agent.domain.agoda.dto.request;

import lombok.Builder;

@Builder
public record DailyRate(

        int minimum,
        int maximum

) {
}
