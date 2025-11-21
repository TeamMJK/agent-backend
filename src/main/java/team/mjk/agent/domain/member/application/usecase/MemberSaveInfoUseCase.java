package team.mjk.agent.domain.member.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.member.application.dto.request.MemberSaveInfoServiceRequest;
import team.mjk.agent.domain.member.domain.Gender;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.global.util.KmsUtil;

@RequiredArgsConstructor
@Service
public class MemberSaveInfoUseCase {

    private final MemberRepository memberRepository;
    private final KmsUtil kmsUtil;

    @Transactional
    public Member saveMemberInfo(MemberSaveInfoServiceRequest request) {
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

        return member;
    }

}
