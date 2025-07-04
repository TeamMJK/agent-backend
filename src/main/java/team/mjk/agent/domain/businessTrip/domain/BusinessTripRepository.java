package team.mjk.agent.domain.businessTrip.domain;

import java.util.List;

public interface BusinessTripRepository {

  void save(BusinessTrip businessTrip);

  BusinessTrip findById(Long businessTripId);

  List<BusinessTrip> findAll();

}