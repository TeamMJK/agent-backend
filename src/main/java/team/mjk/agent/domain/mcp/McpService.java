package team.mjk.agent.domain.mcp;

import team.mjk.agent.domain.businessTrip.application.dto.request.BusinessTripSaveServiceRequest;
import team.mjk.agent.domain.businessTrip.presentation.request.BusinessTripAgentRequest;
import team.mjk.agent.domain.businessTrip.presentation.request.BusinessTripSaveRequest;
import team.mjk.agent.domain.companyworkspace.domain.Workspace;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.receipt.presentation.request.ReceiptMcpRequest;

public interface McpService {

  void createBusinessTrip(BusinessTripSaveServiceRequest request, Long companyId, String name);
  void createBusinessTripAgent(BusinessTripAgentRequest request,Long companyId, String name);
  Workspace getWorkspace();

  void createReceipt(ReceiptMcpRequest request, Long companyId, Member member);
}