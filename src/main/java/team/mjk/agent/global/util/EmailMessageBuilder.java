package team.mjk.agent.global.util;

import org.springframework.stereotype.Component;

@Component
public class EmailMessageBuilder {

    public String buildAuthMessage(String code) {
        return "<br><br>" +
                "인증 번호는 <b>" + code + "</b>입니다." +
                "<br>인증번호를 제대로 입력해주세요.";
    }

    public String buildAuthSubject() {
        return "회원 가입 인증 이메일 입니다.";
    }

}
