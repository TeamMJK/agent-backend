package team.mjk.agent.domain.review.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.review.domain.Review;
import team.mjk.agent.domain.review.domain.ReviewRepository;

@RequiredArgsConstructor
@Repository
public class ReviewRepositoryImpl implements ReviewRepository {

    private final ReviewJpaRepository reviewJpaRepository;

    @Override
    public void save(Review review) {
        reviewJpaRepository.save(review);
    }

    @Override
    public Double findAverageRating() {
        return reviewJpaRepository.findAverageRating();
    }

    @Override
    public boolean existsByMember(Member member) {
        return reviewJpaRepository.existsByMember(member);
    }

}
