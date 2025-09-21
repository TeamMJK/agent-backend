package team.mjk.agent.domain.receipt.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.mjk.agent.domain.company.domain.Workspace;
import team.mjk.agent.domain.receipt.application.ReceiptService;
import team.mjk.agent.domain.receipt.dto.request.ReceiptSaveRequest;
import team.mjk.agent.domain.receipt.dto.request.ReceiptUpdateRequest;
import team.mjk.agent.domain.receipt.dto.response.ImageUploadResponse;
import team.mjk.agent.domain.receipt.dto.response.ReceiptGetResponse;
import team.mjk.agent.domain.receipt.dto.response.ReceiptSaveResponse;
import team.mjk.agent.domain.receipt.dto.response.ReceiptUpdateResponse;
import team.mjk.agent.global.annotation.MemberId;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/receipts")
@RestController
public class ReceiptController implements ReceiptDocsController {

  private final ReceiptService receiptService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ReceiptSaveResponse> saveReceipt(
          @MemberId Long memberId,
          @RequestPart(value = "request") ReceiptSaveRequest request,
          @RequestPart(value = "image", required = false) MultipartFile image
  ) {
    ReceiptSaveResponse response = receiptService.saveReceipt(memberId, request, image);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PatchMapping("/{receiptId}")
  public ResponseEntity<ReceiptUpdateResponse> updateReceipt(
          @MemberId Long memberId,
          @PathVariable Long receiptId,
          @RequestBody ReceiptUpdateRequest request
  ) {
    ReceiptUpdateResponse response = receiptService.updateReceipt(memberId, receiptId, request);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PatchMapping(value = "/upload/{receiptId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ImageUploadResponse> uploadImage(
          @MemberId Long memberId,
          @PathVariable Long receiptId,
          @RequestPart(value = "image", required = false) MultipartFile image
  ) {
    ImageUploadResponse imageUrl = receiptService.upload(memberId, receiptId, image);
    return ResponseEntity.ok(imageUrl);
  }


  @PostMapping(value = "/i/ocr", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<List<Workspace>> ocr(
      @MemberId Long memberId,
      @RequestPart(value = "image", required = false) MultipartFile image
  ) {
    List<Workspace> response = receiptService.saveMcp(memberId, image);
    return new ResponseEntity<>(response, HttpStatus.CREATED);

  }

  @DeleteMapping("/delete")
  public ResponseEntity<?> s3Delete(
      @MemberId Long memberId,
      @RequestParam String addr
  ) {
    receiptService.deleteImageFromS3(memberId, addr);
    return ResponseEntity.ok(null);
  }

  @GetMapping("/{receipt-id}")
  public ResponseEntity<ReceiptGetResponse> getReceipt(
      @MemberId Long memberId,
      @PathVariable("receipt-id") Long receiptId
  ) {
    ReceiptGetResponse response = receiptService.getReceipt(memberId, receiptId);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<ReceiptGetResponse>> getAllReceipt(
      @MemberId Long memberId
  ) {
    List<ReceiptGetResponse> response = receiptService.getAllReceipt(memberId);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{receiptId}")
  public ResponseEntity<Void> deleteReceipt(
      @MemberId Long memberId,
      @PathVariable Long receiptId
  ) {
    receiptService.deleteReceipt(memberId, receiptId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/all")
  public ResponseEntity<List<ReceiptGetResponse>> getAllReceipts() {
    List<ReceiptGetResponse> receipts = receiptService.getAllReceipts();
    return ResponseEntity.ok(receipts);
  }

}