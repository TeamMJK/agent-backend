package team.mjk.agent.domain.companyworkspace.domain;

import jakarta.persistence.*;
import lombok.*;
import team.mjk.agent.domain.company.domain.Company;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class CompanyWorkspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    public static CompanyWorkspace create(Workspace workspace, Company company) {
        return CompanyWorkspace.builder()
                .workspace(workspace)
                .company(company)
                .build();
    }

    public void updateWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

}
