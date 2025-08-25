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
import team.mjk.agent.domain.member.dto.response.*;
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

    EncryptedMemberInfoResponse response = request.encryptWith(kmsUtil);

    member.saveMemberInfo(
            response.name(),
            response.firstName(),
            response.lastName(),
            response.phoneNumber(),
            Gender.valueOf(response.gender()),
            response.birthDate()
    );

    Passport passport = Passport.create(
        response.passportNumber(),
        response.passportExpireDate()
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

    EncryptedMemberInfoResponse response = request.encryptWith(kmsUtil);
    member.update(response);

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
