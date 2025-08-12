package team.mjk.agent.domain.receipt.presentation;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.mjk.agent.domain.receipt.application.ReceiptService;
import team.mjk.agent.domain.receipt.dto.request.ReceiptSaveRequest;
import team.mjk.agent.domain.receipt.dto.response.ReceiptGetResponse;
import team.mjk.agent.domain.receipt.dto.response.ReceiptSaveResponse;
import team.mjk.agent.global.annotation.MemberId;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/receipts")
@RestController
public class ReceiptController implements ReceiptDocsController {

  private final ReceiptService receiptService;

  @PostMapping
  public ResponseEntity<ReceiptSaveResponse> saveReceipt(
      @MemberId Long memberId,
      @RequestBody ReceiptSaveRequest request
  ) {
    ReceiptSaveResponse response = receiptService.saveReceipt(memberId, request);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PostMapping(value = "/i", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<String> s3Upload(
      @MemberId Long memberId,
      @RequestPart(value = "image", required = false) MultipartFile image
  ) {
    String profileImage = receiptService.upload(memberId, image);
    return ResponseEntity.ok(profileImage);
  }

  @PostMapping(value = "/i/ocr", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ReceiptSaveResponse> ocr(
      @MemberId Long memberId,
      @RequestPart(value = "image", required = false) MultipartFile image
  ) {
    ReceiptSaveResponse response = receiptService.ocr(memberId, image);
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

}
