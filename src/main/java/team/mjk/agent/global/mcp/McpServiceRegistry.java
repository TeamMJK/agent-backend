package team.mjk.agent.global.mcp;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;
import team.mjk.agent.domain.company.domain.Workspace;

@Component
public class McpServiceRegistry {

  private final Map<Workspace, McpService> mcpServiceMap = new EnumMap<>(Workspace.class);

  public McpServiceRegistry(List<McpService> services) {
    for (McpService service : services) {
      mcpServiceMap.put(service.getWorkspace(), service);
    }
  }

  public McpService getService(Workspace workspace) {
    return Optional.ofNullable(mcpServiceMap.get(workspace))
        .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 워크스페이스입니다."));
  }
}
