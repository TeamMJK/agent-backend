package team.mjk.agent.domain.receipt.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class InvalidFileExtensionExceptionCode extends CustomException {
    public InvalidFileExtensionExceptionCode() {
        super(ReceiptExceptionCode.INVALID_FILE_EXTENSION);
    }
}
