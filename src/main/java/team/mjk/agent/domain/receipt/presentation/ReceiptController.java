package team.mjk.agent.domain.receipt.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.mjk.agent.domain.receipt.application.ReceiptService;
import team.mjk.agent.domain.receipt.dto.request.ReceiptSaveRequest;
import team.mjk.agent.domain.receipt.dto.response.ReceiptSaveResponse;
import team.mjk.agent.global.annotation.MemberId;

@RequiredArgsConstructor
@RequestMapping("/receipts")
@RestController
public class ReceiptController {

    private final ReceiptService receiptService;

    @PostMapping
    public ResponseEntity<ReceiptSaveResponse> saveReceipt(
            @MemberId Long memberId,
            @RequestBody ReceiptSaveRequest request
    ) {
        ReceiptSaveResponse response = receiptService.saveReceipt(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> s3Upload(
            @MemberId Long memberId,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        String profileImage = receiptService.upload(memberId, image);
        return ResponseEntity.ok(profileImage);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> s3Delete(
            @MemberId Long memberId,
            @RequestParam String addr
    ) {
        receiptService.deleteImageFromS3(memberId, addr);
        return ResponseEntity.ok(null);
    }

}
