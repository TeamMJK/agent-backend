package team.mjk.agent.domain.receipt.application.dto.request;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record ReceiptSaveServiceRequest(

        Long memberId,

        LocalDate paymentDate,

        String approvalNumber,

        String storeAddress,

        BigDecimal totalAmount

) {
}
