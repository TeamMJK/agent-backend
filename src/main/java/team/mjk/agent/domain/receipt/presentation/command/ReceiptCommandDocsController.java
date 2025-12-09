package team.mjk.agent.domain.receipt.presentation.command;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import team.mjk.agent.domain.companyworkspace.domain.Workspace;
import team.mjk.agent.domain.receipt.application.dto.response.ImageUploadResponse;
import team.mjk.agent.domain.receipt.application.dto.response.ReceiptSaveResponse;
import team.mjk.agent.domain.receipt.application.dto.response.ReceiptUpdateResponse;
import team.mjk.agent.domain.receipt.presentation.request.ReceiptSaveRequest;
import team.mjk.agent.domain.receipt.presentation.request.ReceiptUpdateRequest;

import java.util.List;

@Tag(name = "Receipt", description = "영수증 관련 API")
public interface ReceiptCommandDocsController {

    @Operation(summary = "영수증 저장", description = "영수증 데이터와 이미지를 업로드하여 저장합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "영수증 저장 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReceiptSaveResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "회원 또는 회사 정보 없음")
    })
    ResponseEntity<ReceiptSaveResponse> saveReceipt(
            @Parameter(hidden = true) Long memberId,
            @RequestPart(value = "request") ReceiptSaveRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    );

    @Operation(summary = "영수증 수정", description = "기존 영수증 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReceiptUpdateResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "영수증 또는 회원을 찾을 수 없음")
    })
    ResponseEntity<ReceiptUpdateResponse> updateReceipt(
            @Parameter(hidden = true) Long memberId,
            @Parameter(description = "수정할 영수증 ID", required = true) Long receiptId,
            @RequestBody ReceiptUpdateRequest request
    );

    @Operation(summary = "영수증 이미지 업로드", description = "기존 영수증에 이미지를 추가로 업로드합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "이미지 업로드 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ImageUploadResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "영수증 또는 회원을 찾을 수 없음")
    })
    ResponseEntity<ImageUploadResponse> uploadImage(
            @Parameter(hidden = true) Long memberId,
            @Parameter(description = "이미지를 업로드할 영수증 ID", required = true) Long receiptId,
            @RequestPart(value = "image", required = false) MultipartFile image
    );

    @Operation(summary = "OCR 기반 영수증 저장", description = "OCR로 정보를 추출하여 MCP 시스템에 영수증을 저장합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "OCR 처리 및 저장 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Workspace.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "이미지 누락 또는 잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "회원 또는 회사 정보 없음")
    })
    ResponseEntity<List<Workspace>> ocr(
            @Parameter(hidden = true) Long memberId,
            @RequestPart(value = "image", required = false) MultipartFile image
    );

    @Operation(summary = "S3 이미지 삭제", description = "S3에 업로드된 영수증 이미지를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "이미지를 찾을 수 없음")
    })
    ResponseEntity<?> s3Delete(
            @Parameter(hidden = true) Long memberId,
            @Parameter(description = "삭제할 이미지의 S3 URL", required = true) String addr
    );

    @Operation(summary = "영수증 삭제", description = "특정 영수증을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "영수증 또는 회원을 찾을 수 없음")
    })
    ResponseEntity<Void> deleteReceipt(
            @Parameter(hidden = true) Long memberId,
            @Parameter(description = "삭제할 영수증 ID", required = true) Long receiptId
    );

}
