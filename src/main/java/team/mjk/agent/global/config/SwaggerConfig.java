package team.mjk.agent.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.springframework.security.config.Elements.JWT;

@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    private final Environment environment;

    private static final Map<String, String> PROFILE_SERVER_URL_MAP = Map.of(
            "local", "http://localhost:8080",
            "dev", "https://mjk.ai.kr/api"
    );

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .addSecurityItem(securityRequirement())
                .servers(initializeServers())
                .components(components());
    }

    private SecurityRequirement securityRequirement() {
        return new SecurityRequirement().addList(JWT);
    }

    private Info apiInfo() {
        return new Info()
                .title("MJK API")
                .description(getDescription());
    }

    private List<Server> initializeServers() {
        return PROFILE_SERVER_URL_MAP.entrySet().stream()
                .map(entry -> openApiServer(entry.getValue(), "MJK API " + entry.getKey().toUpperCase()))
                .collect(Collectors.toList());
    }

    private Server openApiServer(String url, String description) {
        return new Server().url(url).description(description);
    }

    private Components components() {
        return new Components().addSecuritySchemes(JWT, securityScheme());
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .name(JWT)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat(JWT);
    }

    private String getDescription() {
        return format("""
				MJK API 입니다.\n\n
				엑세스 토큰 값을 넣어주세요.\n\n
				""",
                getLoginUrlByProfile("local"), getLoginUrlByProfile("local"),
                getLoginUrlByProfile("dev"), getLoginUrlByProfile("dev")
        );
    }

    private String getLoginUrlByProfile(String profile) {
        return PROFILE_SERVER_URL_MAP.get(profile) + "/login";
    }
}
