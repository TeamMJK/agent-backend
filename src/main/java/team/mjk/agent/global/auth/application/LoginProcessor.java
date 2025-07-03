package team.mjk.agent.global.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.mjk.agent.global.auth.domain.RefreshToken;
import team.mjk.agent.global.auth.domain.RefreshTokenRepository;
import team.mjk.agent.global.auth.dto.response.LoginResultResponse;
import team.mjk.agent.global.jwt.config.TokenProperties;
import team.mjk.agent.global.jwt.generator.JwtTokenGenerator;

@RequiredArgsConstructor
@Component
public class LoginProcessor {

    private final JwtTokenGenerator tokenGenerator;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProperties tokenProperties;

    public LoginResultResponse generateLoginResult(Long memberId) {
        String accessToken = tokenGenerator.generateAccessToken(memberId);
        String refreshToken = tokenGenerator.generateRefreshToken(memberId);

        RefreshToken refreshTokenEntity = refreshTokenRepository.findByMemberId(memberId)
                .orElse(RefreshToken.of(memberId, refreshToken, tokenProperties.expirationTime().refreshToken()));

        refreshTokenEntity.rotate(refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);

        return new LoginResultResponse(accessToken, refreshToken);
    }

}
