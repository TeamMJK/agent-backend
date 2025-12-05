package team.mjk.agent.domain.review.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.review.application.dto.response.ReviewAverageResponse;
import team.mjk.agent.domain.review.domain.ReviewRepository;

@RequiredArgsConstructor
@Service
public class ReviewQueryService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public ReviewAverageResponse getAverageRating() {
        Double avg = reviewRepository.findAverageRating();
        int rounded = 0;

        if (avg != null) {
            rounded = (int) Math.round(avg);
        }

        return ReviewAverageResponse.from(rounded);
    }

    @Transactional(readOnly = true)
    public boolean hasWrittenReview(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId);

        return reviewRepository.existsByMember(member);
    }

}
