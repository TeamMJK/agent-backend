package team.mjk.agent.domain.mcp.notion.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NotionTokenRequest(
    @NotBlank(message = "token 값을 입력해주세요.")
    String token,

    @NotBlank(message = "출장 database ID를 입력해주세요.")
    @Size(min = 32, max = 32, message = "Database ID는 정확히 32자리여야 합니다.")
    String businessTripDatabaseId,

    @NotBlank(message = "영수증 database ID를 입력해주세요.")
    @Size(min = 32, max = 32, message = "Database ID는 정확히 32자리여야 합니다.")
    String receiptDatabaseId
) {
}