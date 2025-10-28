package team.mjk.agent.domain.test.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import team.mjk.agent.domain.test.application.PythonClientService;

@RestController
@RequiredArgsConstructor
public class PythonClientController {

    private final PythonClientService pythonClientService;

    @PostMapping("/python/test")
    public void testPythonConnection() {
        pythonClientService.sendPostToPython();
    }
}
