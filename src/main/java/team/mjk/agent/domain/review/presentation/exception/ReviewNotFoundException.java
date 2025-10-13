package team.mjk.agent.domain.review.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class ReviewNotFoundException extends CustomException {
    public ReviewNotFoundException() {
        super(ReviewExceptionCode.REVIEW_NOT_FOUND);
    }
}
