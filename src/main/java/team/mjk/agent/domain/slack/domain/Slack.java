package team.mjk.agent.domain.slack.domain;

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
public class Slack {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(columnDefinition = "TEXT")
  private String token;

  @Column(columnDefinition = "TEXT")
  private String channelId;

  private Long companyId;

  @Builder
  private Slack(
      String token,
      String channelId,
      Long companyId
  ) {
    this.token = token;
    this.channelId = channelId;
    this.companyId = companyId;
  }

  public static Slack create(
      String token,
      String channelId,
      Long companyId
  ) {
    return Slack.builder()
        .channelId(channelId)
        .companyId(companyId)
        .token(token)
        .build();
  }

  public void update(
      String token,
      String channelId,
      KmsUtil kmsUtil
  ) {
    this.token = kmsUtil.encrypt(token);
    this.channelId = kmsUtil.encrypt(channelId);
  }

}