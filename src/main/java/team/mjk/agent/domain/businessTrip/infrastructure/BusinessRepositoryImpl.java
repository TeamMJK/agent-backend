package team.mjk.agent.domain.businessTrip.infrastructure;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.mjk.agent.domain.businessTrip.domain.BusinessTrip;
import team.mjk.agent.domain.businessTrip.domain.BusinessTripRepository;
import team.mjk.agent.domain.businessTrip.presentation.exception.BusinessTripInfoNotFoundException;

@RequiredArgsConstructor
@Repository
public class BusinessRepositoryImpl implements BusinessTripRepository {

  private final BusinessJpaRepository businessJpaRepository;

  @Override
  public BusinessTrip save(BusinessTrip businessTrip) {
    return businessJpaRepository.save(businessTrip);
  }

  @Override
  public BusinessTrip findByIdAndCompanyId(Long businessTripId, Long companyId) {
    return businessJpaRepository.findByIdAndCompanyId(businessTripId, companyId)
        .orElseThrow(BusinessTripInfoNotFoundException::new);
  }

  @Override
  public List<BusinessTrip> findAllByCompanyId(Long companyId) {
    return businessJpaRepository.findAllByCompanyId(companyId);
  }

}