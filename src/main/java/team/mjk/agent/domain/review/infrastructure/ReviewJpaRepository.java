package team.mjk.agent.domain.review.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team.mjk.agent.domain.review.domain.Review;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {

    @Query("SELECT AVG(r.rating) FROM Review r")
    Double findAverageRating();

}
