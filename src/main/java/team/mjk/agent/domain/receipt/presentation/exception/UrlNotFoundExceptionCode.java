package team.mjk.agent.domain.receipt.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class UrlNotFoundExceptionCode extends CustomException {
    public UrlNotFoundExceptionCode() {
        super(ReceiptExceptionCode.URL_NOT_FOUND);
    }
}
