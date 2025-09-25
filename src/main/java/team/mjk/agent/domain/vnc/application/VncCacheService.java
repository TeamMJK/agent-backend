package team.mjk.agent.domain.vnc.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import team.mjk.agent.domain.vnc.dto.VncResponse;
import team.mjk.agent.domain.vnc.dto.VncResponseList;
import team.mjk.agent.domain.vnc.domain.VncStatus;

import java.time.Duration;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VncCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final Duration TTL = Duration.ofHours(2);

    public void saveVncResponse(Long memberId, VncResponse vncResponse) {
        String key = "vnc:" + memberId;
        redisTemplate.opsForHash().put(key, vncResponse.session_id(), vncResponse);
        redisTemplate.expire(key, TTL);
    }

    public VncResponse getVncResponse(Long memberId, String sessionId) {
        String key = "vnc:" + memberId;
        return (VncResponse) redisTemplate.opsForHash().get(key, sessionId);
    }

    public List<VncResponse> getAllVncResponses(Long memberId) {
        String key = "vnc:" + memberId;
        return redisTemplate.opsForHash().values(key).stream()
                .map(obj -> (VncResponse) obj)
                .toList();
    }

    public void updateVncStatus(Long memberId, String sessionId, VncStatus newStatus) {
        String key = "vnc:" + memberId;
        VncResponse old = (VncResponse) redisTemplate.opsForHash().get(key, sessionId);
        if (old != null) {
            VncResponse updated = new VncResponse(
                    old.session_id(),
                    old.novnc_url(),
                    old.vncBusinessInfo(),
                    newStatus
            );
            redisTemplate.opsForHash().put(key, sessionId, updated);
        }
    }

    public void saveVncList(Long memberId, VncResponseList list) {
        String key = "vnc:" + memberId;
        list.vncResponseList().forEach(v -> redisTemplate.opsForHash().put(key, v.session_id(), v));
        redisTemplate.expire(key, TTL);
    }

}
