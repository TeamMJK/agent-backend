package team.mjk.agent.domain.mcp.none;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.mjk.agent.domain.businessTrip.domain.BusinessTrip;
import team.mjk.agent.domain.businessTrip.domain.BusinessTripRepository;
import team.mjk.agent.domain.businessTrip.domain.ServiceType;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripAgentRequest;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripSaveRequest;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.company.domain.Workspace;
import team.mjk.agent.domain.mcp.McpService;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.receipt.domain.Receipt;
import team.mjk.agent.domain.receipt.domain.ReceiptRepository;
import team.mjk.agent.domain.receipt.dto.request.ReceiptMcpRequest;

@Service
@RequiredArgsConstructor
public class NoneService implements McpService {

  private final BusinessTripRepository businessTripRepository;
  private final ReceiptRepository receiptRepository;

  @Override
  public void createBusinessTrip(BusinessTripSaveRequest request, Long companyId, String requester) {
    BusinessTrip businessTrip = BusinessTrip.create(
        request.departDate(),
        request.arriveDate(),
        request.destination(),
        request.names(),
        requester,
        companyId,
        request.serviceType()
    );
    businessTripRepository.save(businessTrip);
  }

  @Override
  public void createBusinessTripAgent(BusinessTripAgentRequest request, Long companyId, String requester) {
    BusinessTrip businessTrip = BusinessTrip.create(
        LocalDate.parse(request.departDate()),
        LocalDate.parse(request.arriveDate()),
        request.destination(),
        request.names(),
        requester,
        companyId,
        ServiceType.fromCategory(request.serviceType())
    );
    businessTripRepository.save(businessTrip);
  }

  @Override
  public void createReceipt(ReceiptMcpRequest request, Long companyId, Member member) {
    Company company = member.getValidatedCompany();

    Receipt receipt = Receipt.builder()
        .writer(member.getName())
        .paymentDate(request.paymentDate())
        .approvalNumber(request.approvalNumber())
        .storeAddress(request.storeAddress())
        .memberId(member.getId())
        .totalAmount(request.totalAmount())
        .company(company)
        .url(request.imageUrl())
        .build();

    receiptRepository.save(receipt);
  }

  @Override
  public Workspace getWorkspace() {
    return Workspace.NONE;
  }

}