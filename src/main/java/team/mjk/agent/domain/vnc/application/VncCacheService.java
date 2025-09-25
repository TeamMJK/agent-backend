package team.mjk.agent.domain.vnc.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import team.mjk.agent.domain.vnc.dto.VncResponseList;

import java.time.Duration;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VncCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final Duration TTL = Duration.ofHours(2);

    public void saveVncList(Long memberId, VncResponseList list) {
        redisTemplate.opsForValue().set("vnc:" + memberId, list, TTL);
    }

    public VncResponseList getVncList(Long memberId) {
        Object obj = redisTemplate.opsForValue().get("vnc:" + memberId);
        if (obj == null) {
            return new VncResponseList(List.of());
        }
        return (VncResponseList) obj;
    }

}
