package team.mjk.agent.domain.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.member.domain.Gender;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.member.dto.request.MemberInfoSaveRequest;
import team.mjk.agent.domain.member.dto.request.MemberSaveRequest;
import team.mjk.agent.domain.member.dto.response.MemberInfoSaveResponse;
import team.mjk.agent.domain.member.dto.response.MemberSaveResponse;
import team.mjk.agent.domain.member.presentation.exception.EmailAlreadyExistsException;
import team.mjk.agent.domain.member.presentation.exception.MemberNotFoundException;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberSaveResponse signUp(MemberSaveRequest request) {
        validateEmail(request.email());

        Member member = Member.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();
        memberRepository.save(member);

        return MemberSaveResponse.builder()
                .memberId(member.getId())
                .build();
    }

    @Transactional
    public MemberInfoSaveResponse saveMemberInfo(Long memberId, MemberInfoSaveRequest request) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(MemberNotFoundException::new);

        member.saveMemberInfo(
                request.name(),
                request.firstName(),
                request.lastName(),
                request.phoneNumber(),
                Gender.valueOf(request.gender()),
                request.birthDate()
        );

        return MemberInfoSaveResponse.builder()
                .memberId(member.getId())
                .build();
    }

    private void validateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException();
        }
    }

}
