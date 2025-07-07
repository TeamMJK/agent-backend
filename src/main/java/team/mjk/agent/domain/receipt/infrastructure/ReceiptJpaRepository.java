package team.mjk.agent.domain.receipt.infrastructure;

import io.jsonwebtoken.security.Jwks.OP;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import team.mjk.agent.domain.receipt.domain.Receipt;

import java.util.Optional;

public interface ReceiptJpaRepository extends JpaRepository<Receipt, Long> {

    Optional<Receipt> findByUrl(String url);
    Optional<Receipt> findByIdAndCompanyId(Long id, Long companyId);
    List<Receipt> findAllByCompanyId(Long companyId);
}
