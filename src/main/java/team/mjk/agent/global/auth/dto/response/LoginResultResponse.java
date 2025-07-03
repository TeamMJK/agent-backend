package team.mjk.agent.global.auth.dto.response;

public record LoginResultResponse(

        String accessToken,
        String refreshToken

) {
}
