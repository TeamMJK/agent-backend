package team.mjk.agent.domain.receipt.domain;

import java.util.List;
import java.util.Optional;
import team.mjk.agent.domain.company.domain.Company;

public interface ReceiptRepository {

    void save(Receipt receipt);

    Receipt findByUrl(String url);

    Receipt findByIdAndCompany(Long id, Company company);

    List<Receipt> findAllByCompany(Company company);
}
