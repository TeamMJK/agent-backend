package team.mjk.agent.domain.member.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.member.application.dto.response.MemberInfoGetResponse;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.global.util.KmsUtil;

@RequiredArgsConstructor
@Service
public class MemberQueryService {

    private final MemberRepository memberRepository;
    private final KmsUtil kmsUtil;

    @Transactional(readOnly = true)
    public MemberInfoGetResponse getMemberInfo(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId);

        return MemberInfoGetResponse.from(member, kmsUtil);
    }

}
