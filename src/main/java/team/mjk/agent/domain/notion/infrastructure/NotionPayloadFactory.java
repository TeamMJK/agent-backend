package team.mjk.agent.domain.notion.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripAgentRequest;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripSaveRequest;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.notion.domain.Notion;
import team.mjk.agent.domain.receipt.presentation.request.ReceiptMcpRequest;
import team.mjk.agent.global.util.KmsUtil;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class NotionPayloadFactory {

    private final KmsUtil kmsUtil;

    public Map<String, Object> businessTrip(BusinessTripSaveRequest request, Notion notion, String requester) {

        List<String> names = request.names();
        String dbId = kmsUtil.decrypt(notion.getBusinessTripDatabaseId());

        return Map.of(
                "parent", Map.of("database_id", dbId),
                "properties", Map.of(
                        "출장인원", NotionPropertyBuilder.title(names),
                        "도착일자", NotionPropertyBuilder.richText(request.arriveDate().toString()),
                        "출발일자", NotionPropertyBuilder.richText(request.departDate().toString()),
                        "출장지", NotionPropertyBuilder.richText(request.destination()),
                        "카테고리", NotionPropertyBuilder.richText(request.serviceType().getCategory()),
                        "작성자", NotionPropertyBuilder.richText(requester)
                )
        );
    }

    public Map<String, Object> businessTripAgent(BusinessTripAgentRequest request, Notion notion, String requester) {

        List<String> names = request.names();
        String dbId = kmsUtil.decrypt(notion.getBusinessTripDatabaseId());

        return Map.of(
                "parent", Map.of("database_id", dbId),
                "properties", Map.of(
                        "출장인원", NotionPropertyBuilder.title(names),
                        "도착일자", NotionPropertyBuilder.richText(request.arriveDate()),
                        "출발일자", NotionPropertyBuilder.richText(request.departDate()),
                        "출장지", NotionPropertyBuilder.richText(request.destination()),
                        "카테고리", NotionPropertyBuilder.richText(request.serviceType()),
                        "작성자", NotionPropertyBuilder.richText(requester)
                )
        );
    }

    public Map<String, Object> receipt(ReceiptMcpRequest request, Notion notion, Member member) {

        String dbId = kmsUtil.decrypt(notion.getReceiptDatabaseId());

        return Map.of(
                "parent", Map.of("database_id", dbId),
                "properties", Map.of(
                        "작성자", NotionPropertyBuilder.richText(member.getName()),
                        "승인번호", NotionPropertyBuilder.richText(request.approvalNumber()),
                        "주소", NotionPropertyBuilder.richText(request.storeAddress()),
                        "총금액", NotionPropertyBuilder.richText(request.totalAmount().toString()),
                        "이미지", NotionPropertyBuilder.url(request.imageUrl()),
                        "거래일자", NotionPropertyBuilder.richText(request.paymentDate().toString())
                )
        );
    }

}

