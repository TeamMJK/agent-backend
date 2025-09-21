package team.mjk.agent.domain.receipt.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReceiptUpdateRequest(

        @NotNull(message = "결제일을 입력해주세요.")
        LocalDate paymentDate,

        @NotBlank(message = "승인번호를 입력해주세요.")
        String approvalNumber,

        @NotBlank(message = "싱점 주소를 입력해 주세요.")
        String storeAddress,

        @NotNull(message = "총 금액을 입력해주세요.")
        @DecimalMin(value = "0.0", inclusive = false, message = "총 금액은 0보다 커야 합니다.")
        BigDecimal totalAmount

) {

}

