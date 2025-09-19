package team.mjk.agent.domain.receipt.domain;

import jakarta.persistence.*;
import lombok.*;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.global.domain.BaseTimeEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
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

    public static Receipt create(
            String writer,
            LocalDate paymentDate,
            String approvalNumber,
            String storeAddress,
            BigDecimal totalAmount,
            String url,
            Company company,
            Long memberId
    ) {
        return Receipt.builder()
                .writer(writer)
                .paymentDate(paymentDate)
                .approvalNumber(approvalNumber)
                .storeAddress(storeAddress)
                .totalAmount(totalAmount)
                .url(url)
                .company(company)
                .memberId(memberId)
                .build();
    }

    public void updateUrl(String imageUrl) {
        this.url = imageUrl;
    }

}
