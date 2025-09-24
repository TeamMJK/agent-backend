package team.mjk.agent.domain.vnc.dto;

import java.util.List;

public record SessionIdAndVncList(
    List<VncResponse> vncResponseList
) {

}