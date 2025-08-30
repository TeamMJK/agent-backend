package team.mjk.agent.domain.mcp.notion.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.mjk.agent.global.util.KmsUtil;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

  @Builder
  private Notion(
      String token,
      String BusinessTripDatabaseId,
      String receiptDatabaseId,
      Long companyId
  ) {
    this.token = token;
    this.businessTripDatabaseId = BusinessTripDatabaseId;
    this.companyId = companyId;
    this.receiptDatabaseId = receiptDatabaseId;
  }

  public static Notion create(
      String token,
      String businessTripDatabaseId,
      Long companyId,
      String receiptDatabaseId
  ) {
    return Notion.builder()
        .token(token)
        .BusinessTripDatabaseId(businessTripDatabaseId)
        .companyId(companyId)
        .receiptDatabaseId(receiptDatabaseId)
        .build();
  }

  public void update(String token
      ,String businessTripDatabaseId
      ,KmsUtil kmsUtil
      ,String receiptDatabaseId
  ) {
    this.token = kmsUtil.encrypt(token);
    this.businessTripDatabaseId = kmsUtil.encrypt(businessTripDatabaseId);
    this.receiptDatabaseId = kmsUtil.encrypt(receiptDatabaseId);
  }

}
