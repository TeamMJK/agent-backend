package team.mjk.agent.domain.receipt.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class NoFileExtensionExceptionCode extends CustomException {
    public NoFileExtensionExceptionCode() {
        super(ReceiptExceptionCode.NO_FILE_EXTENSION);
    }
}
