package team.mjk.agent.domain.receipt.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class ReceiptNotFoundExceptionCode extends CustomException {
  public ReceiptNotFoundExceptionCode() {
    super(ReceiptExceptionCode.NOT_FOUND_RECEIPT);
  }
}