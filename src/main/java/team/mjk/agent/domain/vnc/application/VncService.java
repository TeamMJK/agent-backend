package team.mjk.agent.domain.vnc.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.mjk.agent.domain.vnc.domain.VncStatus;
import team.mjk.agent.domain.vnc.dto.VncResponseList;
import team.mjk.agent.domain.vnc.dto.VncSessionIdRequest;
import team.mjk.agent.global.util.AgentResponseUtil;

@RequiredArgsConstructor
@Service
public class VncService {

  private final AgentResponseUtil agentResponseUtil;
  private final VncCacheService vncCacheService;

  public VncResponseList getVncList(Long memberId) {
    return vncCacheService.getVncList(memberId);
  }

  @Transactional
  public VncResponseList pause(Long memberId, VncSessionIdRequest request) {
    vncCacheService.updateVncStatus(memberId, request.sessionId(), VncStatus.PAUSE);
    agentResponseUtil.pauseAgent(request.sessionId(), VncStatus.PAUSE);
    return vncCacheService.getVncList(memberId);
  }

  @Transactional
  public VncResponseList unpause(Long memberId, VncSessionIdRequest request) {
    vncCacheService.updateVncStatus(memberId, request.sessionId(), VncStatus.ING);
    agentResponseUtil.pauseAgent(request.sessionId(), VncStatus.ING);

    return vncCacheService.getVncList(memberId);
  }

}