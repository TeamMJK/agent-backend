package team.mjk.agent.domain.company.domain;

public interface CompanyRepository {

    Company findById(Long id);
    Company save(Company company);

    void delete(Company company);
}