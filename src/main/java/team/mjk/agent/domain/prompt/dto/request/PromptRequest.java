package team.mjk.agent.domain.prompt.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PromptRequest(
    @NotBlank(message = "Prompt 를 입력해주세요.")
    String prompt
) {

}
