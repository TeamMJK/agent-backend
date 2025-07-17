package team.mjk.agent.domain.receipt.infrastructure;

import io.jsonwebtoken.security.Jwks.OP;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.receipt.domain.Receipt;

import java.util.Optional;

public interface ReceiptJpaRepository extends JpaRepository<Receipt, Long> {

    Optional<Receipt> findByUrl(String url);
    Optional<Receipt> findByIdAndCompany(Long id, Company company);
    List<Receipt> findAllByCompany(Company company);
}
