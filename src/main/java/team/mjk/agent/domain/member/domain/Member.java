package team.mjk.agent.domain.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.security.crypto.password.PasswordEncoder;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.member.dto.request.MemberInfoUpdateRequest;
import team.mjk.agent.domain.member.dto.response.MemberInfoGetResponse;
import team.mjk.agent.domain.member.presentation.exception.EmailOrPasswordNotInvalidException;
import team.mjk.agent.domain.passport.domain.Passport;
import team.mjk.agent.global.auth.application.dto.AuthAttributes;
import team.mjk.agent.global.domain.BaseTimeEntity;

import java.time.LocalDate;
import java.util.Objects;

import static jakarta.persistence.EnumType.STRING;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String email;

  private String password;

  private String name;

  private String firstName;

  private String lastName;

  private String phoneNumber;

  @Enumerated(STRING)
  private Gender gender;

  private LocalDate birthDate;

  private String externalId;

  @Enumerated(STRING)
  private LoginProvider loginProvider;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "company_id")
  private Company company;

  @OneToOne
  @JoinColumn(name = "passport_id", unique = true)
  private Passport passport;

  @Builder
  private Member(String email, String password, String externalId, LoginProvider loginProvider) {
    this.email = email;
    this.password = password;
    this.externalId = externalId;
    this.loginProvider = loginProvider;
  }

  public void checkPassword(PasswordEncoder passwordEncoder, String password) {
    if (!passwordEncoder.matches(password, this.password)) {
      throw new EmailOrPasswordNotInvalidException();
    }
  }

  public static Member from(AuthAttributes authAttributes) {
    return Member.builder()
        .email(authAttributes.getEmail())
        .externalId(authAttributes.getExternalId())
        .loginProvider(authAttributes.getProvider())
        .build();
  }

  public boolean hasDifferentProviderWithEmail(String email, String externalId) {
    return Objects.equals(this.email, email) && !Objects.equals(this.externalId, externalId);
  }

  //정적 메서드로 뺄 수 있을듯?
    /*
    public static

    * */
  public void saveMemberInfo(
      String name,
      String firstName,
      String lastName,
      String phoneNumber,
      Gender gender,
      LocalDate birthDate
  ) {
    this.name = name;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phoneNumber = phoneNumber;
    this.gender = gender;
    this.birthDate = birthDate;
  }

  public void savePassport(Passport passport) {
    this.passport = passport;
  }

  public void saveCompany(Company company) {
    this.company = company;
  }

  public void update(MemberInfoUpdateRequest request) {
    this.name = request.name();
    this.firstName = request.firstName();
    this.lastName = request.lastName();
    this.phoneNumber = request.phoneNumber();
    this.gender = Gender.valueOf(request.gender());
    this.birthDate = request.birthDate();
    this.passport.update(request.passportNumber(), request.passportExpireDate());
  }

  public static MemberInfoGetResponse toMemberInfoGetResponse(Member member) {
    return MemberInfoGetResponse.builder()
        .name(member.getName())
        .firstName(member.getFirstName())
        .email(member.getEmail())
        .lastName(member.getLastName())
        .phoneNumber(member.getPhoneNumber())
        .gender(String.valueOf(member.getGender()))
        .birthDate(member.getBirthDate())
        .passportNumber(member.getPassport().getPassportNumber())
        .passportExpireDate(member.getPassport().getPassportExpireDate())
        .build();
  }

}
