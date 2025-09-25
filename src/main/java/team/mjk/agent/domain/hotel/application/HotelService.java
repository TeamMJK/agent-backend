package team.mjk.agent.domain.hotel.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.hotel.dto.HotelAndMemberInfoResponse;
import team.mjk.agent.domain.hotel.dto.VncBusinessInfo;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.vnc.application.VncCacheService;
import team.mjk.agent.domain.vnc.domain.Vnc;
import team.mjk.agent.domain.vnc.domain.VncRepository;
import team.mjk.agent.domain.vnc.dto.VncResponse;
import team.mjk.agent.domain.vnc.dto.VncResponseList;
import team.mjk.agent.domain.member.dto.response.MemberInfoGetResponse;
import team.mjk.agent.global.util.AgentResponseUtil;

@Service
@RequiredArgsConstructor
public class HotelService {

  private final AgentResponseUtil agentResponseUtil;
  private final VncCacheService vncCacheService;
  private final VncRepository vncRepository;
  private final MemberRepository memberRepository;


  @Async
  public void handleHotel(Long memberId, HotelAndMemberInfoResponse response, VncResponseList list) {
    String pythonUrlAgent = "http://127.0.0.1:8000/hotel-agent";
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
                "session",list.vncResponseList().get(index).session_id()
            )
        );
        index++;
        agentResponseUtil.agentResponse(memberId, pythonUrlAgent, payload);
      }
    }
  }

  @Transactional
  public VncResponseList getHotel(Long memberId, HotelAndMemberInfoResponse response) {
    VncResponseList retrunVncResponseList = new VncResponseList(new ArrayList<>());
    String pythonUrlAgent = "http://127.0.0.1:8000/hotel-session";

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

//        VncResponse vncResponse = agentResponseUtil.agentVnc(pythonUrlAgent, payload);
//        VncBusinessInfo vncBusinessInfo = vncResponse.vncBusinessInfo();
//        Member member = memberRepository.findByMemberId(memberId);
//
//        Vnc vnc = Vnc.create(
//                vncResponse.session_id(),
//                vncResponse.novnc_url(),
//                vncResponse.status(),
//                member,
//                vncBusinessInfo.hotel_destination(),
//                vncBusinessInfo.booking_dates(),
//                vncBusinessInfo.guests(),
//                vncBusinessInfo.budget()
//        );
//        vncRepository.save(vnc);
//
//        retrunVncResponseList.vncResponseList().add(vncResponse);

        VncResponse vncResponse = agentResponseUtil.agentVnc(pythonUrlAgent, payload);
        retrunVncResponseList.vncResponseList().add(vncResponse);
      }
    }

    vncCacheService.saveVncList(memberId, retrunVncResponseList);

    return retrunVncResponseList;
  }

}