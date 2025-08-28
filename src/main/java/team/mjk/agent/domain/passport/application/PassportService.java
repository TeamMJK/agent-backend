package team.mjk.agent.domain.passport.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import team.mjk.agent.global.util.KmsUtil;

import java.time.LocalDate;


@Slf4j
@RequiredArgsConstructor
@Service
public class PassportService {

  private final MemberRepository memberRepository;
  private final PassportRepository passportRepository;
  private final KmsUtil kmsUtil;

  @Transactional
  public PassportInfoSaveResponse savePassportInfo(Long memberId, PassportInfoSaveRequest request) {
    Member member = memberRepository.findByMemberId(memberId);

    String encryptedPassportNumber = kmsUtil.encrypt(request.passportNumber());
    String encryptedPassportExpireDate = kmsUtil.encrypt(request.passportExpireDate());

    Passport passport = Passport.create(
            encryptedPassportNumber,
            encryptedPassportExpireDate
    );
    passportRepository.save(passport);

    member.savePassport(passport);

    return PassportInfoSaveResponse.builder()
            .passportId(passport.getId())
            .build();
  }

}