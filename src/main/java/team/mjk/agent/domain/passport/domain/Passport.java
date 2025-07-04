package team.mjk.agent.domain.passport.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Passport {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String passportNumber;

  private LocalDate passportExpireDate;

  @Builder
  private Passport(String passportNumber, LocalDate passportExpireDate) {
    this.passportNumber = passportNumber;
    this.passportExpireDate = passportExpireDate;
  }

  public static Passport create(String passportNumber, LocalDate passportExpireDate) {
    return Passport.builder()
        .passportNumber(passportNumber)
        .passportExpireDate(passportExpireDate)
        .build();
  }

}