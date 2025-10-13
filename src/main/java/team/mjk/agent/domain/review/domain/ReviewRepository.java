package team.mjk.agent.domain.review.domain;

import team.mjk.agent.domain.member.domain.Member;

public interface ReviewRepository {

    void save(Review review);

    Double findAverageRating();

    boolean existsByMember(Member member);

}
