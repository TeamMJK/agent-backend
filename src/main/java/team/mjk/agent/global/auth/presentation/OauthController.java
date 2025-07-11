package team.mjk.agent.global.auth.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/login")
@Controller
public class OauthController implements OauthDocsController {

    @GetMapping
    public String login() {
        return "redirect:/oauth2/authorization/google";
    }

}
