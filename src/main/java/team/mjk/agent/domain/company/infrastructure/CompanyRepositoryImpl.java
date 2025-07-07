package team.mjk.agent.domain.company.infrastructure;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.company.domain.CompanyRepository;

@RequiredArgsConstructor
@Repository
public class CompanyRepositoryImpl implements CompanyRepository {

    private final CompanyJpaRepository companyJpaRepository;
    @Override
    public Optional<Company> findById(Long id) {
        return companyJpaRepository.findById(id);
    }

    @Override
    public Company save(Company company) {
        return companyJpaRepository.save(company);
    }

}
