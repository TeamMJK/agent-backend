package team.mjk.agent.domain.hotel.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import team.mjk.agent.domain.hotel.dto.HotelAndMemberInfoResponse;
import team.mjk.agent.domain.hotel.dto.SessionIdAndVnc;
import team.mjk.agent.domain.hotel.dto.SessionIdAndVncList;
import team.mjk.agent.domain.member.dto.response.MemberInfoGetResponse;
import team.mjk.agent.global.util.AgentResponseUtil;

@Service
@RequiredArgsConstructor
public class HotelService {

  private final AgentResponseUtil agentResponseUtil;

  @Async
  public void handleHotel(Long memberId, HotelAndMemberInfoResponse response,SessionIdAndVncList list) {
    String pythonUrlAgent = "http://1.228.118.20:8000/hotel-agent";
    int index=0;
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
            ),
            "sessionId", Map.of(
                "session",list.sessionIdAndVncList().get(index).session_id()
            )
        );
        index++;
        agentResponseUtil.agentResponse(memberId, pythonUrlAgent, payload);
      }
    }
  }

  public SessionIdAndVncList getHotel(Long memberId, HotelAndMemberInfoResponse response) {
    SessionIdAndVncList retrunSessionIdAndVncList = new SessionIdAndVncList(new ArrayList<>());

    String pythonUrlAgent = "http://1.228.118.20:8000/hotel-session";

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

        SessionIdAndVnc sessionIdAndVnc = agentResponseUtil.agentVnc(pythonUrlAgent, payload);

        retrunSessionIdAndVncList.sessionIdAndVncList().add(sessionIdAndVnc);

      }
    }
    return retrunSessionIdAndVncList;

  }

}