package team.mjk.agent.domain.email.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.mjk.agent.global.util.RedisUtil;

@RequiredArgsConstructor
@Service
public class AuthCodeService {

    private final RedisUtil redisUtil;

    private static final long AUTH_CODE_TTL_SECONDS = 60 * 5L;

    public void saveAuthCode(String code, String email) {
        redisUtil.setDataExpire(code, email, AUTH_CODE_TTL_SECONDS);
    }

    public boolean validateAuthCode(String email, String code) {
        String storedEmail = redisUtil.getData(code);
        return email.equals(storedEmail);
    }

}
