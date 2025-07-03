package team.mjk.agent.global.auth.presentation;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.mjk.agent.global.auth.application.AuthService;
import team.mjk.agent.global.auth.dto.request.LoginResultRequest;
import team.mjk.agent.global.auth.dto.response.LoginResultResponse;

@RequiredArgsConstructor
@RequestMapping("/login")
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<LoginResultResponse> login(
            @Valid @RequestBody LoginResultRequest request,
            HttpServletResponse response) {
        LoginResultResponse result = authService.login(request.email(), request.password(), response);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
