package team.mjk.agent.global.auth.presentation;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.mjk.agent.global.annotation.MemberId;
import team.mjk.agent.global.auth.application.AuthService;
import team.mjk.agent.global.auth.dto.request.LoginResultRequest;
import team.mjk.agent.global.auth.dto.response.LoginResultResponse;

@RequiredArgsConstructor
@RestController
public class AuthController implements AuthDocsController{

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResultResponse> login(
            @Valid @RequestBody LoginResultRequest request,
            HttpServletResponse response
    ) {
        LoginResultResponse result = authService.login(request.email(), request.password(), response);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @MemberId Long memberId,
            HttpServletResponse response
    ) {
        authService.logout(memberId, response);
        return ResponseEntity.ok().build();
    }

}
