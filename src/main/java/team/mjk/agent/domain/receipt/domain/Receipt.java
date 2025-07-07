package team.mjk.agent.domain.receipt.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDate paymentDate;

    private String approvalNumber;

    private String storeAddress;

    private BigDecimal totalAmount;

    private String url;

    private Long companyId;

    @Builder
    private Receipt(
            Member member,
            LocalDate paymentDate,
            String approvalNumber,
            String storeAddress,
            BigDecimal totalAmount,
            String url,
            Long companyId
    ) {
        this.member = member;
        this.paymentDate = paymentDate;
        this.approvalNumber = approvalNumber;
        this.storeAddress = storeAddress;
        this.totalAmount = totalAmount;
        this.url = url;
        this.companyId = companyId;
    }

}
