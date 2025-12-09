package team.mjk.agent.domain.slack.infrastructure;

import org.springframework.stereotype.Component;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripSaveRequest;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripAgentRequest;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.receipt.presentation.request.ReceiptMcpRequest;

import java.util.List;

@Component
public class SlackPayloadFactory {

    public String businessTrip(BusinessTripSaveRequest req, String requester) {
        List<String> members = req.names();
        String memberStr = String.join(", ", members);

        return String.format(
                """
                작성자: %s
                출장 예약: %s ~ %s
                장소: %s
                숙박/항공: %s
                출장 인원: %s
                """,
                requester,
                req.departDate(),
                req.arriveDate(),
                req.destination(),
                req.serviceType().getCategory(),
                memberStr
        );
    }

    public String businessTripAgent(BusinessTripAgentRequest req, String requester) {
        List<String> members = req.names();
        String memberStr = String.join(", ", members);

        return String.format(
                """
                작성자: %s
                출장 예약: %s ~ %s
                장소: %s
                숙박/항공: %s
                출장 인원: %s
                """,
                requester,
                req.departDate(),
                req.arriveDate(),
                req.destination(),
                req.serviceType(),
                memberStr
        );
    }

    public String receipt(ReceiptMcpRequest req, Member member) {
        return String.format(
                """
                작성자: %s
                날짜: %s
                주문번호: %s
                주소: %s
                총금액: %s
                이미지 주소: %s
                """,
                member.getName(),
                req.paymentDate(),
                req.approvalNumber(),
                req.storeAddress(),
                req.totalAmount(),
                req.imageUrl()
        );
    }

}
