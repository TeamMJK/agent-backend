package team.mjk.agent.domain.mcp;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;
import team.mjk.agent.domain.companyworkspace.domain.Workspace;
import team.mjk.agent.domain.mcp.exception.McpNotFoundException;

@Component
public class McpServiceRegistry {

  private final Map<Workspace, List<McpService>> mcpServiceMap = new EnumMap<>(Workspace.class);

  public McpServiceRegistry(List<McpService> services) {
    for (McpService service : services) {
      mcpServiceMap.computeIfAbsent(service.getWorkspace(), k -> new ArrayList<>()).add(service);
    }
  }

  public List<McpService> getServices(Workspace workspace) {
    return Optional.ofNullable(mcpServiceMap.get(workspace))
        .orElseThrow(McpNotFoundException::new);
  }

}