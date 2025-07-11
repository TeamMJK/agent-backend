package team.mjk.agent.domain.notion.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import team.mjk.agent.domain.notion.dto.request.NotionTokenRequest;

@Tag(name = "Notion", description = "노션 관련 API")
@RequestMapping("/notions")
public interface NotionDocsController {

  @Operation(summary = "노션 등록", description = "노션 api key 값과 databaseId 값을 등록합니다.")
  @ApiResponses({
      @ApiResponse(
          responseCode = "201",
          description = "노션 등록 성공",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))
      )
  })
  ResponseEntity<Long> saveNotion(
      @Parameter(hidden = true) Long memberId,
      @RequestBody(description = "노션 등록 요청 DTO", required = true) NotionTokenRequest request
  );

}
