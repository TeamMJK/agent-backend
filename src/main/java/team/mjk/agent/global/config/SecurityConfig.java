package team.mjk.agent.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private static final String[] PERMIT_ALL_PATTERNS = {
            "/login/**",
            "/members",
            "/swagger-ui/**",
            "/actuator/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/oauth2/**",
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        disableSecurityBasic(httpSecurity);
        configureSessionManagement(httpSecurity);
        configureApiAuthorization(httpSecurity);
        configureCorsPolicy(httpSecurity);

        return httpSecurity.build();
    }

    private void disableSecurityBasic(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);
    }

    private void configureSessionManagement(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(STATELESS));
    }


    private void configureApiAuthorization(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(authorize ->
                authorize.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .requestMatchers(PERMIT_ALL_PATTERNS).permitAll()
                        .anyRequest().authenticated()
        );
    }

    private void configureCorsPolicy(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(cors -> cors.configurationSource(request -> {
            var corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowedOrigins(List.of(
                    "http://localhost:8080", "http://localhost:3000"));
            corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS"));
            corsConfiguration.setAllowedHeaders(List.of("*"));
            corsConfiguration.setExposedHeaders(List.of("Authorization", "Set-Cookie"));
            corsConfiguration.setAllowCredentials(true);
            return corsConfiguration;
        }));
    }


}
