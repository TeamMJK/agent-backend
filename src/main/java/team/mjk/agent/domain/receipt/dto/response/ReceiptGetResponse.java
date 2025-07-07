package team.mjk.agent.domain.receipt.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record ReceiptGetResponse(
    LocalDate paymentDate,

    String approvalNumber,

    String storeAddress,

    BigDecimal totalAmount
) {

}
