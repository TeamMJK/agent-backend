package team.mjk.agent.domain.prompt.dto.response;

import java.util.List;

public record Flight(
  String departure,
  String arrival,
  String depart_date,
  String return_date,
  int guests,
  List<String> names,
  List<String> requirements,
  int budget
) {

}