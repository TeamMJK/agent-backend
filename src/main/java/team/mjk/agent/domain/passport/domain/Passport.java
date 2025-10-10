package team.mjk.agent.domain.passport.domain;

import jakarta.persistence.*;
import lombok.*;

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

  public static Passport create(String passportNumber, String passportExpireDate) {
    return Passport.builder()
        .passportNumber(passportNumber)
        .passportExpireDate(passportExpireDate)
        .build();
  }

  public void update(String passportNumber, String passportExpireDate) {
    if (passportNumber != null) {
      this.passportNumber = passportNumber;
    }
    if (passportExpireDate != null) {
      this.passportExpireDate = passportExpireDate;
    }
  }

}