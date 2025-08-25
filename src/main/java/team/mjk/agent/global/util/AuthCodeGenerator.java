package team.mjk.agent.global.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class AuthCodeGenerator {

    private static final SecureRandom random = new SecureRandom();

    public String generateCode(int length) {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

}
