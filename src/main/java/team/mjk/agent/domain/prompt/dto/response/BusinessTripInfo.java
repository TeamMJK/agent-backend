package team.mjk.agent.domain.prompt.dto.response;

public record BusinessTripInfo(
  String departure,
  String arrival,
  String depart_date,
  String return_date,
  int guests
) {

}