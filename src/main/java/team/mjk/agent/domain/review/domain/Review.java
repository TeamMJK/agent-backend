package team.mjk.agent.domain.review.domain;

import jakarta.persistence.*;
import lombok.*;
import team.mjk.agent.domain.member.domain.Member;
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

    @Column(nullable = false)
    private String reviewContent;

    @Column(nullable = false)
    private int rating;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public static Review create(String reviewContent, int rating, Member member) {
        return Review.builder()
                .reviewContent(reviewContent)
                .rating(rating)
                .member(member)
                .build();
    }

}
