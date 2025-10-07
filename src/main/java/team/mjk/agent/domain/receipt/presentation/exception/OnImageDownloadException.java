package team.mjk.agent.domain.receipt.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class OnImageDownloadException extends CustomException {
    public OnImageDownloadException() {
        super(ReceiptExceptionCode.ON_IMAGE_DOWNLOAD);
    }
}
