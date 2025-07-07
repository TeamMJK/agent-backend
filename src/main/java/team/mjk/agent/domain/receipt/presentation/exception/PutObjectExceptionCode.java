package team.mjk.agent.domain.receipt.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class PutObjectExceptionCode extends CustomException {
    public PutObjectExceptionCode() {
        super(ReceiptExceptionCode.PUT_OBJECT_EXCEPTION);
    }
}
