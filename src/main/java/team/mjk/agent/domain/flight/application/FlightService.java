package team.mjk.agent.domain.flight.application;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import team.mjk.agent.domain.flight.dto.FlightAndMemberInfoResponse;
import team.mjk.agent.domain.member.application.dto.response.MemberInfoGetResponse;
import team.mjk.agent.global.util.AgentResponseUtil;

@Service
@RequiredArgsConstructor
public class FlightService {

  private final AgentResponseUtil agentResponseUtil;

  @Async
  public void handleFlight(Long memberId,FlightAndMemberInfoResponse response) {

    System.out.println("response:"+response);
    String pythonUrl = "http://localhost:8000/flight-data";

    for (var flight : response.flightList().flights()) {
      List<MemberInfoGetResponse> matchedMembers = response.memberInfoList()
          .memberInfoGetResponseList().stream()
          .filter(member -> flight.names().stream()
              .anyMatch(flightName -> member.name().contains(flightName)))
          .toList();
      System.out.println("flightNames: "+flight.names());
      List<Map<String, String>> memberPayloads = matchedMembers.stream().map(member -> Map.of(
          "name", member.name(),
          "firstName", member.firstName(),
          "lastName", member.lastName(),
          "email", member.email(),
          "phoneNumber", member.phoneNumber(),
          "gender", member.gender(),
          "birthDate", member.birthDate(),
          "passportNumber", member.passportNumber(),
          "passportExpireDate", member.passportExpireDate()
      )).toList();

      Map<String, Object> payload = Map.of(
          "flight", Map.of(
              "depart_date", flight.depart_date(),
              "return_date", flight.return_date(),
              "departure", flight.departure(),
              "arrival", flight.arrival(),
              "guests", flight.guests(),
              "requirements", flight.requirements(),
              "names", flight.names(),
              "budget", flight.budget()
          ),
          "memberInfoList", memberPayloads
      );

      agentResponseUtil.agentResponse(memberId, pythonUrl, payload);
    }
  }

}