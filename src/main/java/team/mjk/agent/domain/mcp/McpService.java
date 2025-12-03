package team.mjk.agent.domain.mcp;

import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripAgentRequest;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripSaveRequest;
import team.mjk.agent.domain.companyworkspace.domain.Workspace;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.receipt.dto.request.ReceiptMcpRequest;

public interface McpService {

  void createBusinessTrip(BusinessTripSaveRequest request,Long companyId, String name);
  void createBusinessTripAgent(BusinessTripAgentRequest request,Long companyId, String name);
  Workspace getWorkspace();

  void createReceipt(ReceiptMcpRequest request, Long companyId, Member member);
}