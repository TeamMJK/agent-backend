package team.mjk.agent.domain.receipt.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class OnImageUploadExceptionCode extends CustomException {
    public OnImageUploadExceptionCode() {
        super(ReceiptExceptionCode.ON_IMAGE_UPLOAD);
    }
}
