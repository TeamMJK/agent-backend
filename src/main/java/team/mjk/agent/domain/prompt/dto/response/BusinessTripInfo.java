package team.mjk.agent.domain.prompt.dto.response;

import java.util.List;

public record BusinessTripInfo(
  String departure,
  String arrival,
  String depart_date,
  String return_date,
  int guests,
  List<String> names
) {

}