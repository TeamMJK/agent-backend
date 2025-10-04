package team.mjk.agent.domain.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.company.domain.CompanyRepository;
import team.mjk.agent.domain.member.application.dto.request.MemberSaveInfoServiceRequest;
import team.mjk.agent.domain.member.application.dto.request.MemberSaveServiceRequest;
import team.mjk.agent.domain.member.application.dto.request.MemberUpdateInfoServiceRequest;
import team.mjk.agent.domain.member.application.dto.response.MemberInfoGetResponse;
import team.mjk.agent.domain.member.application.dto.response.MemberSaveInfoResponse;
import team.mjk.agent.domain.member.application.dto.response.MemberUpdateInfoResponse;
import team.mjk.agent.domain.member.domain.Gender;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.member.presentation.exception.EmailAlreadyExistsException;
import team.mjk.agent.domain.member.application.dto.response.MemberSaveResponse;
import team.mjk.agent.global.util.KmsUtil;

@RequiredArgsConstructor
@Service
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final CompanyRepository companyRepository;
  private final KmsUtil kmsUtil;

  public MemberSaveResponse signUp(MemberSaveServiceRequest request) {
    validateEmail(request.email());

    Member member = Member.create(request.email(), passwordEncoder.encode(request.password()));
    memberRepository.save(member);

    return MemberSaveResponse.builder()
            .memberId(member.getId())
            .build();
  }

  @Transactional
  public MemberSaveInfoResponse saveMemberInfo(MemberSaveInfoServiceRequest request) {
    Member member = memberRepository.findByMemberId(request.memberId());

    member.saveMemberInfo(
            request.name(),
            request.firstName(),
            request.lastName(),
            request.phoneNumber(),
            Gender.valueOf(request.gender()),
            request.birthDate(),
            request.passportNumber(),
            request.passportExpireDate(),
            kmsUtil
    );

    return MemberSaveInfoResponse.builder()
        .memberId(member.getId())
        .build();
  }

  public MemberInfoGetResponse getMemberInfo(Long memberId) {
    Member member = memberRepository.findByMemberId(memberId);

    return MemberInfoGetResponse.from(member, kmsUtil);
  }

  @Transactional
  public MemberUpdateInfoResponse updateMemberInfo(MemberUpdateInfoServiceRequest request) {
    Member member = memberRepository.findByMemberId(request.memberId());

    member.updateMemberInfo(
            request.name(),
            request.firstName(),
            request.lastName(),
            request.phoneNumber(),
            Gender.valueOf(request.gender()),
            request.birthDate(),
            request.passportNumber(),
            request.passportExpireDate(),
            kmsUtil
    );

    return MemberUpdateInfoResponse.builder()
        .memberId(member.getId())
        .build();
  }

  @Transactional
  public Long delete(Long memberId) {
    Member member = memberRepository.findByMemberId(memberId);
    Company company = member.getCompany();

    if (company != null) {
      long remainCount = memberRepository.countByCompanyId(company.getId());
      if (remainCount == 0) {
        companyRepository.delete(company);
      }
    }

    memberRepository.delete(memberId);

    return memberId;
  }

  private void validateEmail(String email) {
    if (memberRepository.existsByEmail(email)) {
      throw new EmailAlreadyExistsException();
    }
  }

}
