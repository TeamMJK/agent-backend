package team.mjk.agent.global.mcp;

import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripSaveRequest;
import team.mjk.agent.domain.company.domain.Workspace;
import team.mjk.agent.domain.receipt.dto.request.ReceiptMcpRequest;
import team.mjk.agent.domain.receipt.dto.request.ReceiptSaveRequest;

public interface McpService {

  void createBusinessTrip(BusinessTripSaveRequest request,Long companyId);
  Workspace getWorkspace();

  void createReceipt(ReceiptMcpRequest request, Long companyId);
}
