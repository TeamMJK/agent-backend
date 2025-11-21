package team.mjk.agent.domain.member.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team.mjk.agent.domain.member.application.dto.request.MemberSaveServiceRequest;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.member.presentation.exception.EmailAlreadyExistsException;

@RequiredArgsConstructor
@Service
public class MemberSignUpUseCase {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member signUp(MemberSaveServiceRequest request) {
        if (memberRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException();
        }

        Member member = Member.create(
                request.email(),
                passwordEncoder.encode(request.password())
        );
        memberRepository.save(member);

        return member;
    }

}
