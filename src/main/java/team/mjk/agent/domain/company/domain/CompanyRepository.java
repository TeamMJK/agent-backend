package team.mjk.agent.domain.company.domain;

import java.util.Optional;

public interface CompanyRepository {

    Company findById(Long id);
    Company save(Company company);

}