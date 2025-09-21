package team.mjk.agent.domain.receipt.dto.response;

import lombok.Builder;

@Builder
public record ImageUploadResponse(

        String imageUrl

) {
}
