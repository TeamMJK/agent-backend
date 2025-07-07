package team.mjk.agent.domain.receipt.dto.response;

import java.util.List;
import lombok.Builder;
import team.mjk.agent.domain.receipt.domain.Receipt;

@Builder
public record ReceiptGetAllResponse(
    List<Receipt> receiptList
) {

}
