package team.mjk.agent.domain.receipt.presentation.query;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import team.mjk.agent.domain.receipt.application.dto.response.ReceiptGetResponse;

import java.util.List;

@Tag(name = "Receipt", description = "영수증 관련 API")
public interface ReceiptQueryDocsController {

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

}
