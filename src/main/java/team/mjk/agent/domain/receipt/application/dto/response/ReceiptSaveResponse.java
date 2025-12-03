package team.mjk.agent.domain.receipt.application.dto.response;

import lombok.Builder;

@Builder
public record ReceiptSaveResponse(

        Long receiptId

) {
}
