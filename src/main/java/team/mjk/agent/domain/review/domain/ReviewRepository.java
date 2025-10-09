package team.mjk.agent.domain.review.domain;

public interface ReviewRepository {

    void save(Review review);

    Double findAverageRating();

}
