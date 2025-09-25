package team.mjk.agent.domain.businessTrip.domain;

import java.util.List;

public interface BusinessTripRepository {

  BusinessTrip save(BusinessTrip businessTrip);

  BusinessTrip findByIdAndCompanyId(Long businessTripId, Long companyId);

  List<BusinessTrip> findAllByCompanyId(Long companyId);

  void delete(BusinessTrip businessTrip);

  void deleteAllByCompanyId(Long companyId);

}