package team.mjk.agent.domain.vnc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import team.mjk.agent.domain.hotel.dto.VncBusinessInfo;
import team.mjk.agent.domain.vnc.domain.Vnc;
import team.mjk.agent.domain.vnc.domain.VncStatus;

@Builder
public record SessionIdAndVnc(
    String session_id,
    String novnc_url,
    VncBusinessInfo vncBusinessInfo,
    VncStatus status
) {

}