package team.mjk.agent.domain.flight.dto;

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