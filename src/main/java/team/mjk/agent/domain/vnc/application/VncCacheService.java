package team.mjk.agent.domain.vnc.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import team.mjk.agent.domain.vnc.dto.VncResponse;
import team.mjk.agent.domain.vnc.dto.VncResponseList;
import team.mjk.agent.domain.vnc.domain.VncStatus;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VncCacheService {

    private final RedisTemplate<String, VncResponseList> redisTemplate;
    private static final Duration TTL = Duration.ofHours(2);

    public void saveVnc(Long memberId, VncResponse newResponse) {
        String key = "vnc:" + memberId;
        VncResponseList existingList = redisTemplate.opsForValue().get(key);

        if (existingList == null) {
            existingList = new VncResponseList(new ArrayList<>());
        }

        existingList.vncResponseList().add(newResponse);
        redisTemplate.opsForValue().set(key, existingList, TTL);
    }

    public VncResponseList getVncList(Long memberId) {
        String key = "vnc:" + memberId;
        VncResponseList list = redisTemplate.opsForValue().get(key);
        return list != null ? list : new VncResponseList(List.of());
    }

    public void updateVncStatus(Long memberId, String sessionId, VncStatus newStatus) {
        VncResponseList list = getVncList(memberId);
        List<VncResponse> updatedList = list.vncResponseList().stream()
                .map(v -> v.session_id().equals(sessionId)
                        ? new VncResponse(v.session_id(), v.novnc_url(), v.vncBusinessInfo(), newStatus)
                        : v
                )
                .collect(Collectors.toList());

        redisTemplate.opsForValue().set("vnc:" + memberId, new VncResponseList(updatedList), TTL);
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
