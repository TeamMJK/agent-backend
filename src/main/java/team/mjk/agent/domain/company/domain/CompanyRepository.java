package team.mjk.agent.domain.company.domain;

public interface CompanyRepository {

    Company findByCompanyId(Long companyId);

    Company save(Company company);

    void delete(Company company);

}