package team.mjk.agent.domain.company.application.dto.response;

import lombok.Builder;

@Builder
public record CompanyUpdateResponse(

        Long companyId

) {
}
