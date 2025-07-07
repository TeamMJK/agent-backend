package team.mjk.agent.domain.receipt.domain;

import java.util.List;
import java.util.Optional;

public interface ReceiptRepository {

    void save(Receipt receipt);

    Receipt findByUrl(String url);

    Receipt findByIdAndCompanyId(Long id, Long companyId);

    List<Receipt> findAllByCompanyId(Long companyId);
}
