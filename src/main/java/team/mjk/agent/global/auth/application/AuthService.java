package team.mjk.agent.global.auth.application;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.member.presentation.exception.MemberNotFoundException;
import team.mjk.agent.global.auth.dto.response.LoginResultResponse;
import team.mjk.agent.global.jwt.injector.TokenInjector;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginProcessor loginProcessor;
    private final TokenInjector tokenInjector;

    @Transactional
    public LoginResultResponse login(String email, String password, HttpServletResponse response) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
        member.checkPassword(passwordEncoder, password);

        LoginResultResponse result = loginProcessor.generateLoginResult(member.getId());

        tokenInjector.injectTokensToCookie(result, response);

        return loginProcessor.generateLoginResult(member.getId());
    }

}
