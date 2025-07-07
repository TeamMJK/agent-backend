package team.mjk.agent.domain.receipt.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReceiptSaveRequest(

        LocalDate paymentDate,

        String approvalNumber,

        String storeAddress,

        BigDecimal totalAmount

) {
}
