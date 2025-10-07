package team.mjk.agent.domain.receipt.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import team.mjk.agent.domain.receipt.domain.Receipt;

@Builder
public record ReceiptGetResponse(

    Long receiptId,

    String writer,

    LocalDate paymentDate,

    String approvalNumber,

    String storeAddress,

    BigDecimal totalAmount,

    String url

) {

    public static ReceiptGetResponse from(Receipt receipt) {
        return ReceiptGetResponse.builder()
                .receiptId(receipt.getId())
                .writer(receipt.getWriter())
                .paymentDate(receipt.getPaymentDate())
                .approvalNumber(receipt.getApprovalNumber())
                .storeAddress(receipt.getStoreAddress())
                .totalAmount(receipt.getTotalAmount())
                .url(receipt.getUrl())
                .build();
    }

}
