package team.mjk.agent.domain.receipt.infrastructure;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.mjk.agent.domain.receipt.domain.Receipt;
import team.mjk.agent.domain.receipt.domain.ReceiptRepository;
import team.mjk.agent.domain.receipt.presentation.exception.ReceiptNotFoundExceptionCode;
import team.mjk.agent.domain.receipt.presentation.exception.UrlNotFoundExceptionCode;

@RequiredArgsConstructor
@Repository
public class ReceiptRepositoryImpl implements ReceiptRepository {

  private final ReceiptJpaRepository receiptJpaRepository;

  @Override
  public void save(Receipt receipt) {
    receiptJpaRepository.save(receipt);
  }

  @Override
  public Receipt findByUrl(String url) {
    return receiptJpaRepository.findByUrl(url)
        .orElseThrow(UrlNotFoundExceptionCode::new);
  }

  @Override
  public Receipt findByIdAndCompanyId(Long id, Long companyId) {
    return receiptJpaRepository.findByIdAndCompanyId(id, companyId)
        .orElseThrow(ReceiptNotFoundExceptionCode::new);
  }

    @Override
    public List<Receipt> findAllByCompanyId(Long companyId) {
        return receiptJpaRepository.findAllByCompanyId(companyId);
    }


}
