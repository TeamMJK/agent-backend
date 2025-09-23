package team.mjk.agent.domain.hotel.dto;

import lombok.Builder;

@Builder
public record SessionIdAndVnc(
    String session_id,
    String novnc_url,
    Detail detail
) {

}
