package team.mjk.agent.domain.receipt.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class UpdateNotForbiddenExceptionCode extends CustomException {
    public UpdateNotForbiddenExceptionCode() {
        super(ReceiptExceptionCode.UPDATE_NOT_FORBIDDEN);
    }
}
