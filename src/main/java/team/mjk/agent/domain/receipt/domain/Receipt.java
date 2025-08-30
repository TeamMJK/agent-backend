package team.mjk.agent.domain.receipt.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.global.domain.BaseTimeEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Receipt extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String writer;

    private LocalDate paymentDate;

    private String approvalNumber;

    private String storeAddress;

    private BigDecimal totalAmount;

    private Long memberId;

    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Builder
    private Receipt(
            String writer,
            LocalDate paymentDate,
            String approvalNumber,
            String storeAddress,
            BigDecimal totalAmount,
            String url,
            Company company,
            Long memberId
    ) {
        this.writer = writer;
        this.paymentDate = paymentDate;
        this.approvalNumber = approvalNumber;
        this.storeAddress = storeAddress;
        this.totalAmount = totalAmount;
        this.url = url;
        this.company = company;
        this.memberId = memberId;
    }

}
