package team.mjk.agent.domain.receipt.presentation.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;

@Builder
public record ReceiptMcpRequest(

        LocalDate paymentDate,

        String approvalNumber,

        String storeAddress,

        BigDecimal totalAmount,

        String imageUrl

) {

}