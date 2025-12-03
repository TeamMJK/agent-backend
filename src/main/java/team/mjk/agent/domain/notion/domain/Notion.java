package team.mjk.agent.domain.notion.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import team.mjk.agent.global.util.KmsUtil;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class Notion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String token;

    @Column(columnDefinition = "TEXT")
    private String businessTripDatabaseId;

    @Column(columnDefinition = "TEXT")
    private String receiptDatabaseId;

    private Long companyId;

    public static Notion create(
            String token,
            String businessTripDatabaseId,
            String receiptDatabaseId,
            Long companyId
    ) {
        return Notion.builder()
                .token(token)
                .businessTripDatabaseId(businessTripDatabaseId)
                .receiptDatabaseId(receiptDatabaseId)
                .companyId(companyId)
                .build();
    }

    public void update(
            String token,
            String businessTripDatabaseId,
            String receiptDatabaseId,
            KmsUtil kmsUtil
    ) {
        this.token = kmsUtil.encrypt(token);
        this.businessTripDatabaseId = kmsUtil.encrypt(businessTripDatabaseId);
        this.receiptDatabaseId = kmsUtil.encrypt(receiptDatabaseId);
    }

}
