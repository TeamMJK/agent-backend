package team.mjk.agent.domain.receipt.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class DeleteNotForbiddenExceptionCode extends CustomException {
    public DeleteNotForbiddenExceptionCode() {
        super(ReceiptExceptionCode.DELETE_NOT_FORBIDDEN);
    }
}
