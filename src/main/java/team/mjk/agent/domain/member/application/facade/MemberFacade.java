package team.mjk.agent.domain.member.application.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.mjk.agent.domain.member.application.dto.request.MemberSaveInfoServiceRequest;
import team.mjk.agent.domain.member.application.dto.request.MemberSaveServiceRequest;
import team.mjk.agent.domain.member.application.dto.request.MemberUpdateInfoServiceRequest;
import team.mjk.agent.domain.member.application.dto.response.MemberInfoGetResponse;
import team.mjk.agent.domain.member.application.dto.response.MemberSaveInfoResponse;
import team.mjk.agent.domain.member.application.dto.response.MemberSaveResponse;
import team.mjk.agent.domain.member.application.dto.response.MemberUpdateInfoResponse;
import team.mjk.agent.domain.member.application.usecase.*;
import team.mjk.agent.domain.member.domain.Member;

@RequiredArgsConstructor
@Service
public class MemberFacade {

    private final MemberSignUpUseCase memberSignUpUseCase;
    private final MemberSaveInfoUseCase memberSaveInfoUseCase;
    private final MemberUpdateInfoUseCase memberUpdateInfoUseCase;
    private final MemberGetInfoUseCase memberGetInfoUseCase;
    private final MemberDeleteUseCase memberDeleteUseCase;

    public MemberSaveResponse signUp(MemberSaveServiceRequest request) {
        Member member = memberSignUpUseCase.signUp(request);

        return MemberSaveResponse.builder()
                .memberId(member.getId())
                .build();
    }

    public MemberSaveInfoResponse saveMemberInfo(MemberSaveInfoServiceRequest request) {
        Member member = memberSaveInfoUseCase.saveMemberInfo(request);

        return MemberSaveInfoResponse.builder()
                .memberId(member.getId())
                .build();
    }

    public MemberUpdateInfoResponse updateMemberInfo(MemberUpdateInfoServiceRequest request) {
        Member member = memberUpdateInfoUseCase.updateMemberInfo(request);

        return MemberUpdateInfoResponse.builder()
                .memberId(member.getId())
                .build();
    }

    public MemberInfoGetResponse getMemberInfo(Long memberId) {
        return memberGetInfoUseCase.getMemberInfo(memberId);
    }

    public Long delete(Long memberId) {
        return memberDeleteUseCase.delete(memberId);
    }

}
