package team.mjk.agent.domain.receipt.presentation.query;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.mjk.agent.domain.receipt.application.query.ReceiptQueryService;
import team.mjk.agent.domain.receipt.application.dto.response.ReceiptGetResponse;
import team.mjk.agent.global.annotation.MemberId;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/receipts")
@RestController
public class ReceiptQueryController implements ReceiptQueryDocsController {

    private final ReceiptQueryService receiptQueryService;

    @GetMapping("/{receipt-id}")
    public ResponseEntity<ReceiptGetResponse> getReceipt(
            @MemberId Long memberId,
            @PathVariable("receipt-id") Long receiptId
    ) {
        ReceiptGetResponse response = receiptQueryService.getReceipt(memberId, receiptId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ReceiptGetResponse>> getAllReceipt(
            @MemberId Long memberId
    ) {
        List<ReceiptGetResponse> response = receiptQueryService.getAllReceipt(memberId);
        return ResponseEntity.ok(response);
    }

}
