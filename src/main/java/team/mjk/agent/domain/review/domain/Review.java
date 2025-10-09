package team.mjk.agent.domain.review.domain;

import jakarta.persistence.*;
import lombok.*;
import team.mjk.agent.global.domain.BaseTimeEntity;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reviewContent;

    @Column(nullable = false)
    private int rating;

    public static Review create(String reviewContent, int rating) {
        return Review.builder()
                .reviewContent(reviewContent)
                .rating(rating)
                .build();
    }

}
