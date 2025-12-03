package team.mjk.agent.domain.notion.application.dto.request;

import lombok.Builder;

@Builder
public record NotionConfigSaveServiceRequest(

        Long companyId,

        String token,

        String businessTripDatabaseId,

        String receiptDatabaseId

) {
}
