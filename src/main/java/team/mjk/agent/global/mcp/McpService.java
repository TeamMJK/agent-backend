package team.mjk.agent.global.mcp;

import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripSaveRequest;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.company.domain.Workspace;

public interface McpService {

  void create(BusinessTripSaveRequest request,Long companyId);
  Workspace getWorkspace();

}
