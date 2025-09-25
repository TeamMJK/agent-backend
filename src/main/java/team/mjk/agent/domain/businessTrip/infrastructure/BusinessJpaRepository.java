package team.mjk.agent.domain.businessTrip.infrastructure;

import io.jsonwebtoken.security.Jwks.OP;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.mjk.agent.domain.businessTrip.domain.BusinessTrip;

public interface BusinessJpaRepository extends JpaRepository<BusinessTrip, Long> {
  Optional<BusinessTrip> findByIdAndCompanyId(Long businessTripId, Long companyId);
  List<BusinessTrip> findAllByCompanyId(Long companyId);

  @Modifying
  @Query("delete from BusinessTrip b where b.companyId = :companyId")
  void deleteAllByCompanyId(@Param("companyId") Long companyId);

}