package team.mjk.agent.domain.passport.domain;

import jakarta.persistence.*;

import lombok.*;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.global.util.KmsUtil;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class Passport {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(columnDefinition = "TEXT")
  private String passportNumber;

  @Column(columnDefinition = "TEXT")
  private String passportExpireDate;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", unique = true)
  private Member member;


  public static Passport create(String passportNumber, String passportExpireDate, Member member) {
    return Passport.builder()
        .passportNumber(passportNumber)
        .passportExpireDate(passportExpireDate)
        .member(member)
        .build();
  }

  public void update(String passportNumber, String passportExpireDate, KmsUtil kmsUtil) {
    if (passportNumber != null) {
      this.passportNumber = kmsUtil.encrypt(passportNumber);
    }
    if (passportExpireDate != null) {
      this.passportExpireDate = kmsUtil.encrypt(passportExpireDate);
    }
  }

}