package team.mjk.agent.domain.company.infrastructure;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.company.domain.CompanyRepository;
import team.mjk.agent.domain.company.presentation.exception.CompanyNotFoundException;

@RequiredArgsConstructor
@Repository
public class CompanyRepositoryImpl implements CompanyRepository {

    private final CompanyJpaRepository companyJpaRepository;

    @Override
    public Company findByCompanyId(Long companyId) {
        return companyJpaRepository.findById(companyId)
            .orElseThrow(CompanyNotFoundException::new);
    }

    @Override
    public Company save(Company company) {
        return companyJpaRepository.save(company);
    }

    @Override
    public void delete(Company company) {
        companyJpaRepository.delete(company);
    }

}
