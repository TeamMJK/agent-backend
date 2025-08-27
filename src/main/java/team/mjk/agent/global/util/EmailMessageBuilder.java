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

    public String buildInvitationMessage(String code) {
        return """
            <p>안녕하세요,</p>
            <p>아래 초대 코드를 사용해 가입을 완료해 주세요.</p>
            <b>초대 코드: %s</b>
        """.formatted(code);
    }

    public String buildInvitationSubject() {
        return "회사 초대 코드 안내";
    }

}
