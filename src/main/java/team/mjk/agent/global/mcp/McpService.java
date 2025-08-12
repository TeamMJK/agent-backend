package team.mjk.agent.global.mcp;

import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripSaveRequest;
import team.mjk.agent.domain.company.domain.Workspace;

public interface McpService {

  void createBusinessTrip(BusinessTripSaveRequest request,Long companyId);
  Workspace getWorkspace();

}
