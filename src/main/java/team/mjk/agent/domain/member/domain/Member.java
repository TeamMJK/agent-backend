package team.mjk.agent.domain.member.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.company.presentation.exception.CompanyNotFoundException;
import team.mjk.agent.domain.member.dto.request.MemberInfoUpdateRequest;
import team.mjk.agent.domain.member.dto.response.EncryptedMemberInfoResponse;
import team.mjk.agent.domain.member.dto.response.MemberInfoGetResponse;
import team.mjk.agent.domain.member.presentation.exception.EmailOrPasswordNotInvalidException;
import team.mjk.agent.domain.passport.domain.Passport;
import team.mjk.agent.global.auth.application.dto.AuthAttributes;
import team.mjk.agent.global.domain.BaseTimeEntity;
import team.mjk.agent.global.util.KmsUtil;

import java.time.LocalDate;
import java.util.Objects;

import static jakarta.persistence.EnumType.STRING;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String email;

    private String password;

    @Column(columnDefinition = "TEXT")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String firstName;

    @Column(columnDefinition = "TEXT")
    private String lastName;

    @Column(columnDefinition = "TEXT")
    private String phoneNumber;

    @Enumerated(STRING)
    private Gender gender;

    @Column(columnDefinition = "TEXT")
    private String birthDate;

    private String externalId;

    @Enumerated(STRING)
    private LoginProvider loginProvider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne
    @JoinColumn(name = "passport_id", unique = true)
    private Passport passport;

    public static Member create(String email, String password) {
        return Member.builder()
                .email(email)
                .password(password)
                .build();
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

    public void saveMemberInfo(
            String name,
            String firstName,
            String lastName,
            String phoneNumber,
            Gender gender,
            String birthDate
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

    public void update(EncryptedMemberInfoResponse response) {
        this.name = response.name();
        this.firstName = response.firstName();
        this.lastName = response.lastName();
        this.phoneNumber = response.phoneNumber();
        this.gender = Gender.valueOf(response.gender());
        this.birthDate = response.birthDate();

        this.passport.update(
                response.passportNumber(),
                response.passportExpireDate()
        );
    }

    public Company getCompany(){
        if(this.company == null) {
            throw new CompanyNotFoundException();
        }
        return this.company;
    }

    public static MemberInfoGetResponse toMemberInfoGetResponse(Member member, KmsUtil kmsUtil) {
        return MemberInfoGetResponse.builder()
                .name(member.getName())
                .firstName(kmsUtil.decrypt(member.getFirstName()))
                .email(member.getEmail())
                .lastName(kmsUtil.decrypt(member.getLastName()))
                .phoneNumber(kmsUtil.decrypt(member.getPhoneNumber()))
                .gender(String.valueOf(member.getGender()))
                .birthDate(kmsUtil.decrypt(member.getBirthDate()))
                .passportNumber(kmsUtil.decrypt(member.getPassport().getPassportNumber()))
                .passportExpireDate(kmsUtil.decrypt(member.getPassport().getPassportExpireDate()))
                .build();
    }

    public void updateCompany(Company company) {
        this.company = company;
    }

}