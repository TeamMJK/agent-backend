package team.mjk.agent.domain.passport.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.passport.application.dto.request.PassportSaveServiceRequest;
import team.mjk.agent.domain.passport.application.dto.request.PassportUpdateServiceRequest;
import team.mjk.agent.domain.passport.application.dto.response.PassportInfoResponse;
import team.mjk.agent.domain.passport.application.dto.response.PassportSaveResponse;
import team.mjk.agent.domain.passport.application.dto.response.PassportUpdateResponse;
import team.mjk.agent.domain.passport.domain.Passport;
import team.mjk.agent.domain.passport.domain.PassportRepository;
import team.mjk.agent.global.util.KmsUtil;

@RequiredArgsConstructor
@Service
public class PassportService {

    private final PassportRepository passportRepository;
    private final MemberRepository memberRepository;
    private final KmsUtil kmsUtil;

    @Transactional
    public PassportSaveResponse savePassport(PassportSaveServiceRequest request) {
        Member member = memberRepository.findByMemberId(request.memberId());

        Passport passport = Passport.builder()
                .passportNumber(kmsUtil.encrypt(request.passportNumber()))
                .passportExpireDate(kmsUtil.encrypt(request.passportExpireDate()))
                .member(member)
                .build();
        passportRepository.save(passport);

        return PassportSaveResponse.builder()
                .passPortId(passport.getId())
                .build();
    }

    @Transactional
    public PassportUpdateResponse updatePassport(PassportUpdateServiceRequest request) {
        Passport passport = passportRepository.findByMemberId(request.memberId());

        passport.update(
                request.passportNumber(),
                request.passportExpireDate(),
                kmsUtil
        );

        return PassportUpdateResponse.builder()
                .passPortId(passport.getId())
                .build();
    }

    @Transactional(readOnly = true)
    public PassportInfoResponse getPassportInfo(Long memberId) {
        Passport passport = passportRepository.findByMemberId(memberId);

        return PassportInfoResponse.from(passport, kmsUtil);
    }

}
