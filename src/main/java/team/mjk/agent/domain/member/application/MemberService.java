package team.mjk.agent.domain.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.member.domain.Gender;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.member.dto.request.MemberInfoSaveRequest;
import team.mjk.agent.domain.member.dto.request.MemberInfoUpdateRequest;
import team.mjk.agent.domain.member.dto.request.MemberSaveRequest;
import team.mjk.agent.domain.member.dto.response.MemberInfoGetResponse;
import team.mjk.agent.domain.member.dto.response.MemberInfoSaveResponse;
import team.mjk.agent.domain.member.dto.response.MemberInfoUpdateResponse;
import team.mjk.agent.domain.member.dto.response.MemberSaveResponse;
import team.mjk.agent.domain.member.presentation.exception.EmailAlreadyExistsException;
import team.mjk.agent.domain.member.presentation.exception.MemberNotFoundException;
import team.mjk.agent.domain.passport.domain.Passport;
import team.mjk.agent.domain.passport.domain.PassportRepository;
import team.mjk.agent.global.util.KmsUtil;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final PassportRepository passportRepository;
    private final KmsUtil kmsUtil;

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

        String encryptName = kmsUtil.encrypt(request.name());
        String encryptFirstName = kmsUtil.encrypt(request.firstName());
        String encryptLastName = kmsUtil.encrypt(request.lastName());
        String encryptPhoneNumber = kmsUtil.encrypt(request.phoneNumber());
        String encryptBirthDate = kmsUtil.encrypt(request.birthDate());

        member.saveMemberInfo(
                encryptName,
                encryptFirstName,
                encryptLastName,
                encryptPhoneNumber,
                Gender.valueOf(request.gender()),
                encryptBirthDate
        );

        Passport passport = Passport.create(
                request.passportNumber(),
                request.passportExpireDate()
        );
        passportRepository.save(passport);

        member.savePassport(passport);
        return MemberInfoSaveResponse.builder()
                .memberId(member.getId())
                .build();
    }

    public MemberInfoGetResponse getMemberInfo(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(MemberNotFoundException::new);

        return Member.toMemberInfoGetResponse(member, kmsUtil);
    }

    @Transactional
    public MemberInfoUpdateResponse updateMemberInfo(Long memberId, MemberInfoUpdateRequest request) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(MemberNotFoundException::new);

        member.update(request);

        return MemberInfoUpdateResponse.builder()
                .memberId(member.getId())
                .build();
    }

    private void validateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException();
        }
    }

}
