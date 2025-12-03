package team.mjk.agent.domain.receipt.application.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record ReceiptUpdateServiceRequest(

        Long memberId,

        Long receiptId,

        LocalDate paymentDate,

        String approvalNumber,

        String storeAddress,

        BigDecimal totalAmount

) {
}
