package team.mjk.agent.domain.vnc.dto;

import lombok.Builder;
import team.mjk.agent.domain.hotel.dto.VncBusinessInfo;
import team.mjk.agent.domain.vnc.domain.VncStatus;

@Builder
public record VncResponse(
    String session_id,
    String novnc_url,
    VncBusinessInfo vncBusinessInfo,
    VncStatus status
) {

}