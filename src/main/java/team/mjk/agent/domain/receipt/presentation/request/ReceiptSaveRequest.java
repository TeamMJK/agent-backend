package team.mjk.agent.domain.receipt.presentation.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import team.mjk.agent.domain.receipt.application.dto.request.ReceiptSaveServiceRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReceiptSaveRequest(

        @NotNull(message = "결제 날짜를 선택해 주세요.")
        LocalDate paymentDate,

        @NotBlank(message = "승인 번호를 입력해 주세요.")
        String approvalNumber,

        @NotBlank(message = "가맹점 주소를 입력해 주세요.")
        String storeAddress,

        @NotNull(message = "총 결제 금액을 입력해 주세요.")
        @DecimalMin(value = "0.0", inclusive = false, message = "총 금액은 0보다 커야 합니다.")
        BigDecimal totalAmount

) {

    public ReceiptSaveServiceRequest toServiceRequest(Long memberId) {
        return ReceiptSaveServiceRequest.builder()
                .memberId(memberId)
                .paymentDate(paymentDate)
                .approvalNumber(approvalNumber)
                .storeAddress(storeAddress)
                .totalAmount(totalAmount)
                .build();
    }

}
