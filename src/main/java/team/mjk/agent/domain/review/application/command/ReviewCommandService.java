package team.mjk.agent.domain.review.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.review.application.dto.request.ReviewSaveServiceRequest;
import team.mjk.agent.domain.review.application.dto.response.ReviewSaveResponse;
import team.mjk.agent.domain.review.domain.Review;
import team.mjk.agent.domain.review.domain.ReviewRepository;

@RequiredArgsConstructor
@Service
public class ReviewCommandService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ReviewSaveResponse saveReview(ReviewSaveServiceRequest request) {
        Member member = memberRepository.findByMemberId(request.memberId());

        Review review = Review.builder()
                .reviewContent(request.reviewContent())
                .rating(request.rating())
                .member(member)
                .build();
        reviewRepository.save(review);

        return ReviewSaveResponse.builder()
                .reviewId(review.getId())
                .build();
    }

}
