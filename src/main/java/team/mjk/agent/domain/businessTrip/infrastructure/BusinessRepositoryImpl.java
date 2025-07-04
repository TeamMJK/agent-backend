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
  public void save(BusinessTrip businessTrip) {
    businessJpaRepository.save(businessTrip);
  }

  @Override
  public BusinessTrip findById(Long businessTripId) {
    return businessJpaRepository.findById(businessTripId)
        .orElseThrow(BusinessTripInfoNotFoundException::new);
  }

  @Override
  public List<BusinessTrip> findAll() {
    return businessJpaRepository.findAll();
  }

}