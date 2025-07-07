package team.mjk.agent.domain.receipt.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class EmptyFileExceptionCode extends CustomException {
    public EmptyFileExceptionCode() {
        super(ReceiptExceptionCode.EMPTY_FILE_EXCEPTION);
    }
}
