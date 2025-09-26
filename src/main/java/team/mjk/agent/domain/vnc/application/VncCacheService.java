package team.mjk.agent.domain.vnc.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import team.mjk.agent.domain.vnc.dto.VncResponse;
import team.mjk.agent.domain.vnc.dto.VncResponseList;
import team.mjk.agent.domain.vnc.domain.VncStatus;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import team.mjk.agent.domain.vnc.presentation.exception.FailRedisAgentConnectExceptionCode;

@RequiredArgsConstructor
@Service
public class VncCacheService {

    private final RedisTemplate<String, VncResponseList> redisTemplate;
    private static final Duration TTL = Duration.ofHours(2);

    public void saveVncList(Long memberId, VncResponseList list) {
        try {
            String key = "vnc:" + memberId;
            redisTemplate.opsForValue().set(key, list, TTL);
        } catch (Exception e) {
            throw new FailRedisAgentConnectExceptionCode();
        }
    }

    public VncResponseList getVncList(Long memberId) {
        String key = "vnc:" + memberId;
        VncResponseList list = redisTemplate.opsForValue().get(key);
        if (list == null) {
            return new VncResponseList(List.of());
        }
        return list;
    }

    public void updateVncStatus(Long memberId, String sessionId, VncStatus newStatus) {
        VncResponseList list = getVncList(memberId);
        List<VncResponse> updatedList = list.vncResponseList().stream()
                .map(v -> v.session_id().equals(sessionId)
                        ? new VncResponse(v.session_id(), v.novnc_url(), v.vncBusinessInfo(), newStatus)
                        : v
                )
                .collect(Collectors.toList());

        saveVncList(memberId, new VncResponseList(updatedList));
    }

    public List<VncResponse> getAllVncResponses(Long memberId) {
        return getVncList(memberId).vncResponseList();
    }

    public VncResponse getVncResponse(Long memberId, String sessionId) {
        return getVncList(memberId).vncResponseList().stream()
                .filter(v -> v.session_id().equals(sessionId))
                .findFirst()
                .orElse(null);
    }

}
