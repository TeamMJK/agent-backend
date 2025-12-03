package team.mjk.agent.domain.receipt.presentation.command;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.mjk.agent.domain.company.domain.Workspace;
import team.mjk.agent.domain.receipt.application.command.ReceiptCommandService;
import team.mjk.agent.domain.receipt.application.dto.response.ImageUploadResponse;
import team.mjk.agent.domain.receipt.application.dto.response.ReceiptSaveResponse;
import team.mjk.agent.domain.receipt.application.dto.response.ReceiptUpdateResponse;
import team.mjk.agent.domain.receipt.presentation.request.ReceiptSaveRequest;
import team.mjk.agent.domain.receipt.presentation.request.ReceiptUpdateRequest;
import team.mjk.agent.global.annotation.MemberId;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/receipts")
@RestController
public class ReceiptCommandController implements ReceiptCommandDocsController {

    private final ReceiptCommandService receiptCommandService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReceiptSaveResponse> saveReceipt(
            @MemberId Long memberId,
            @RequestPart(value = "request") ReceiptSaveRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        ReceiptSaveResponse response = receiptCommandService.saveReceipt(request.toServiceRequest(memberId), image);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{receiptId}")
    public ResponseEntity<ReceiptUpdateResponse> updateReceipt(
            @MemberId Long memberId,
            @PathVariable Long receiptId,
            @RequestBody ReceiptUpdateRequest request
    ) {
        ReceiptUpdateResponse response = receiptCommandService.updateReceipt(request.toServiceRequest(memberId, receiptId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping(value = "/upload/{receiptId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageUploadResponse> uploadImage(
            @MemberId Long memberId,
            @PathVariable Long receiptId,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        ImageUploadResponse imageUrl = receiptCommandService.upload(memberId, receiptId, image);
        return ResponseEntity.ok(imageUrl);
    }


    @PostMapping(value = "/i/ocr", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<Workspace>> ocr(
            @MemberId Long memberId,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        List<Workspace> response = receiptCommandService.saveMcp(memberId, image);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> s3Delete(
            @MemberId Long memberId,
            @RequestParam String addr
    ) {
        receiptCommandService.deleteImageFromS3(memberId, addr);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{receiptId}")
    public ResponseEntity<Void> deleteReceipt(
            @MemberId Long memberId,
            @PathVariable Long receiptId
    ) {
        receiptCommandService.deleteReceipt(memberId, receiptId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
