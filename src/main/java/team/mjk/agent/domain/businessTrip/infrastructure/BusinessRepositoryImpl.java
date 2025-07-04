package team.mjk.agent.domain.businessTrip.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.mjk.agent.domain.businessTrip.domain.BusinessTrip;
import team.mjk.agent.domain.businessTrip.domain.BusinessTripRepository;

@RequiredArgsConstructor
@Repository
public class BusinessRepositoryImpl implements BusinessTripRepository {

  private final BusinessJpaRepository businessJpaRepository;

  @Override
  public void save(BusinessTrip businessTrip) {
    businessJpaRepository.save(businessTrip);
  }

}
