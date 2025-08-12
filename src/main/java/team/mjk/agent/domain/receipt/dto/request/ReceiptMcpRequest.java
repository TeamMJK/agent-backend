package team.mjk.agent.domain.receipt.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record ReceiptMcpRequest(
    LocalDate paymentDate,

    String approvalNumber,

    String storeAddress,

    BigDecimal totalAmount,

    String name,

    String imageUrl
) {

}