package team.mjk.agent.domain.receipt.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import team.mjk.agent.domain.receipt.dto.request.ReceiptSaveRequest;
import team.mjk.agent.domain.receipt.dto.response.ReceiptGetResponse;
import team.mjk.agent.domain.receipt.dto.response.ReceiptSaveResponse;

import java.util.List;

@Tag(name = "Receipt", description = "영수증 관련 API")
public interface ReceiptDocsController {

    @Operation(summary = "영수증 저장", description = "영수증 데이터를 저장합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "영수증 저장 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReceiptSaveResponse.class)
                    )
            )
    })
    ResponseEntity<ReceiptSaveResponse> saveReceipt(
            @Parameter(hidden = true) Long memberId,
            @RequestBody(description = "영수증 저장 요청 DTO", required = true)
            ReceiptSaveRequest request
    );

    @Operation(summary = "영수증 이미지 업로드", description = "S3에 영수증 이미지를 업로드합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업로드 성공")
    })
    ResponseEntity<String> s3Upload(
            @Parameter(hidden = true) Long memberId,
            @RequestPart(required = false) MultipartFile image
    );

    @Operation(summary = "영수증 이미지 삭제", description = "S3에서 영수증 이미지를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공")
    })
    ResponseEntity<?> s3Delete(
            @Parameter(hidden = true) Long memberId,
            @Parameter(description = "삭제할 이미지 주소", required = true) String addr
    );

    @Operation(summary = "영수증 단건 조회", description = "특정 영수증을 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReceiptGetResponse.class)
                    )
            )
    })
    ResponseEntity<ReceiptGetResponse> getReceipt(
            @Parameter(hidden = true) Long memberId,
            @Parameter(description = "조회할 영수증 ID", required = true) Long receiptId
    );

    @Operation(summary = "전체 영수증 조회", description = "회원의 전체 영수증 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReceiptGetResponse.class)
                    )
            )
    })
    ResponseEntity<List<ReceiptGetResponse>> getAllReceipt(
            @Parameter(hidden = true) Long memberId
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
