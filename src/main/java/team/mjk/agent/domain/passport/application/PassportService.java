package team.mjk.agent.domain.passport.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.member.domain.Gender;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.member.dto.request.MemberInfoSaveRequest;
import team.mjk.agent.domain.member.dto.response.MemberInfoSaveResponse;
import team.mjk.agent.domain.member.presentation.exception.MemberNotFoundException;
import team.mjk.agent.domain.passport.domain.Passport;
import team.mjk.agent.domain.passport.domain.PassportRepository;
import team.mjk.agent.domain.passport.dto.request.PassportInfoSaveRequest;
import team.mjk.agent.domain.passport.dto.response.PassportInfoSaveResponse;


@RequiredArgsConstructor
@Service
public class PassportService {

  private final MemberRepository memberRepository;
  private final PassportRepository passportRepository;

  @Transactional
  public PassportInfoSaveResponse savePassportInfo(Long memberId, PassportInfoSaveRequest request) {
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(MemberNotFoundException::new);

    Passport passport = Passport.create(
        request.passportNumber(),
        request.passportExpireDate()
    );
    passportRepository.save(passport);

    member.savePassport(passport);

    return PassportInfoSaveResponse.builder()
        .passportId(passport.getId())
        .build();
  }

}