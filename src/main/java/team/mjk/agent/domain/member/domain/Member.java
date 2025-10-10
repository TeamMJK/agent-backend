package team.mjk.agent.domain.member.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.company.presentation.exception.CompanyNotFoundException;
import team.mjk.agent.domain.member.presentation.exception.EmailOrPasswordNotInvalidException;
import team.mjk.agent.domain.passport.domain.Passport;
import team.mjk.agent.global.auth.application.dto.AuthAttributes;
import team.mjk.agent.global.domain.BaseTimeEntity;
import team.mjk.agent.global.util.KmsUtil;

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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "passport_id", unique = true)
    private Passport passport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

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
            String birthDate,
            String passportNumber,
            String passportExpireDate,
            KmsUtil kmsUtil
    ) {
        this.name = name;
        this.firstName = kmsUtil.encrypt(firstName);
        this.lastName = kmsUtil.encrypt(lastName);
        this.phoneNumber = kmsUtil.encrypt(phoneNumber);
        this.gender = gender;
        this.birthDate = kmsUtil.encrypt(birthDate);
        this.passport = Passport.create(
                kmsUtil.encrypt(passportNumber),
                kmsUtil.encrypt(passportExpireDate)
        );
    }

    public void saveCompany(Company company) {
        this.company = company;
    }

    public void updateMemberInfo(
            String name,
            String firstName,
            String lastName,
            String phoneNumber,
            Gender gender,
            String birthDate,
            String passportNumber,
            String passportExpireDate,
            KmsUtil kmsUtil
    ) {
        this.name = name;
        this.firstName = kmsUtil.encrypt(firstName);
        this.lastName = kmsUtil.encrypt(lastName);
        this.phoneNumber = kmsUtil.encrypt(phoneNumber);
        this.gender = gender;
        this.birthDate = kmsUtil.encrypt(birthDate);
        this.passport.update(
                kmsUtil.encrypt(passportNumber),
                kmsUtil.encrypt(passportExpireDate)
        );
    }

    public Company getValidatedCompany() {
        if (this.company == null) {
            throw new CompanyNotFoundException();
        }
        return this.company;
    }

}