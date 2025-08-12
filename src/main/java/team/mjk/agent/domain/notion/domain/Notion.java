package team.mjk.agent.domain.notion.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aspectj.weaver.ast.Not;
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
  private String databaseId;

  private Long companyId;

  @Builder
  private Notion(
      String token,
      String databaseId,
      Long companyId
  ) {
    this.token = token;
    this.databaseId = databaseId;
    this.companyId = companyId;
  }

  public static Notion create(
      String token,
      String databaseId,
      Long companyId
  ) {
    return Notion.builder()
        .token(token)
        .databaseId(databaseId)
        .companyId(companyId)
        .build();
  }

  public void update(String token, String databaseId, KmsUtil kmsUtil) {
    this.token = kmsUtil.encrypt(token);
    this.databaseId = kmsUtil.encrypt(databaseId);
  }

}
