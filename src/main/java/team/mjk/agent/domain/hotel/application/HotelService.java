package team.mjk.agent.domain.hotel.application;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import team.mjk.agent.domain.hotel.dto.HotelAndMemberInfoResponse;
import team.mjk.agent.domain.member.dto.response.MemberInfoGetResponse;
import team.mjk.agent.global.util.AgentResponseUtil;

@Service
@RequiredArgsConstructor
public class HotelService {

  private final AgentResponseUtil agentResponseUtil;

  @Async
  public void handleHotel(Long memberId, HotelAndMemberInfoResponse response) {

    String pythonUrl = "http://localhost:8000/hotel-data";
    for (var hotel : response.hotelList().hotels()) {
      List<MemberInfoGetResponse> matchedMembers = response.memberInfoList()
          .memberInfoGetResponseList().stream()
          .filter(member -> hotel.names().stream()
              .anyMatch(hotelName -> member.name().contains(hotelName)))
          .toList();

      if (!matchedMembers.isEmpty()) {
        MemberInfoGetResponse firstMember = matchedMembers.get(0);

        Map<String, Object> payload = Map.of(
            "hotel", Map.of(
                "departure_date", hotel.departure_date(),
                "arrival_date", hotel.arrival_date(),
                "destination", hotel.destination(),
                "guests", hotel.guests(),
                "budget", hotel.budget(),
                "requirements", hotel.requirements(),
                "names", hotel.names()
            ),
            "memberInfo", Map.of(
                "name", firstMember.name(),
                "firstName", firstMember.firstName(),
                "lastName", firstMember.lastName(),
                "email", firstMember.email(),
                "phoneNumber", firstMember.phoneNumber(),
                "gender", firstMember.gender(),
                "birthDate", firstMember.birthDate(),
                "passportNumber", firstMember.passportNumber(),
                "passportExpireDate", firstMember.passportExpireDate()
            )
        );

        agentResponseUtil.agentResponse(memberId, pythonUrl, payload);
      }
    }
  }

}