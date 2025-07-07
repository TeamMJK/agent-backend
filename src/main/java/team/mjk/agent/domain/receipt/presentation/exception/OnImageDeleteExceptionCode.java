package team.mjk.agent.domain.receipt.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class OnImageDeleteExceptionCode extends CustomException {
    public OnImageDeleteExceptionCode() {
        super(ReceiptExceptionCode.ON_IMAGE_DELETE);
    }
}
