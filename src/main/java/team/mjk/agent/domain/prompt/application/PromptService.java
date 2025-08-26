package team.mjk.agent.domain.prompt.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team.mjk.agent.domain.businessTrip.application.BusinessTripService;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripAgentRequest;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.member.dto.response.MemberInfoGetResponse;
import team.mjk.agent.domain.member.dto.response.MemberInfoList;
import team.mjk.agent.domain.member.presentation.exception.MemberNotFoundException;
import team.mjk.agent.domain.prompt.dto.request.PromptRequest;
import team.mjk.agent.domain.prompt.dto.response.FlightAndMemberInfoResponse;
import team.mjk.agent.domain.prompt.dto.response.FlightList;
import team.mjk.agent.domain.prompt.dto.response.HotelAndMemberInfoResponse;
import team.mjk.agent.domain.prompt.dto.response.HotelList;
import team.mjk.agent.domain.prompt.dto.response.IntegrationResponse;
import team.mjk.agent.domain.prompt.dto.response.NameList;
import team.mjk.agent.global.util.KmsUtil;

@RequiredArgsConstructor
@Service
public class PromptService {

  private final ChatClient chatClient;
  private final MemberRepository memberRepository;
  private final KmsUtil kmsUtil;
  private final RestTemplate restTemplate = new RestTemplate();
  private final BusinessTripService businessTripService;
  private final ObjectMapper objectMapper;

  public void handleHotel(Long memberId, PromptRequest request) {
    HotelAndMemberInfoResponse response = extractHotel(memberId, request);

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
            "memberInfo", Map.of(    // 단일 멤버 정보
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

        // POST 요청 실행
        agentResponse(memberId, pythonUrl, payload);
      }
    }
  }

  public void handleFlight(Long memberId, PromptRequest request) {
    FlightAndMemberInfoResponse response = extractFlight(memberId,request);
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

      agentResponse(memberId, pythonUrl, payload);
    }
  }

  private void agentResponse(Long memberId, String pythonUrl, Map<String, Object> payload) {
    String result = restTemplate.postForObject(
        pythonUrl,
        payload,
        String.class
    );
    try {
      JsonNode rootNode = objectMapper.readTree(result);
      JsonNode detailNode = rootNode.get("detail");
      String detailJson = detailNode.toString();
      System.out.println("result :" + result);
      BusinessTripAgentRequest agentRequest = objectMapper.readValue(detailJson, BusinessTripAgentRequest.class);

      System.out.println("names" + agentRequest.names());
      businessTripService.saveAgentMcp(memberId, agentRequest);
    } catch (JsonProcessingException e) {
      System.out.println(e.getMessage());
    }
  }


  public HotelAndMemberInfoResponse extractHotel(Long memberId, PromptRequest request) {
    HotelList hotelList = extractHotelInfo(memberId, request);
    MemberInfoList memberInfoList = extractNames(memberId, request);

    return new HotelAndMemberInfoResponse(hotelList, memberInfoList);
  }

  public FlightAndMemberInfoResponse extractFlight(Long memberId,
      PromptRequest request) {
    FlightList flightList = extractFlightInfo(memberId, request);
    MemberInfoList memberInfoList = extractNames(memberId, request);

    return new FlightAndMemberInfoResponse(flightList, memberInfoList);
  }

  public IntegrationResponse extractIntegration(Long memberId, PromptRequest request) {
    FlightList flightList = extractFlightInfo(memberId, request);
    HotelList hotelList = extractHotelInfo(memberId, request);
    MemberInfoList memberInfoList = extractNames(memberId, request);

    return new IntegrationResponse(flightList, hotelList, memberInfoList);
  }

  public HotelList extractHotelInfo(Long memberId, PromptRequest request) {
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(MemberNotFoundException::new);

    String fullPrompt = String.format(
        """
            다음 문장에서 출장 정보를 추출해줘. 올해는 2025년이야. 예산은 숙박 일수 만큼 나눠.
            출발일은 departure_date 에 저장하고 도착일은 arrival_date 에 저장해.
            문장을 파악해서 요청자와 같이 출장을 가는 사람 이름이면 그것에 맞춰 인원 수 추가.
            만약 9월 23일부터 4박 5일이면 depart_date = 2025-09-23, return_date = 2025-09-27.
            출장 정보 한 개당 최소 한 명의 이름 필요.
            만약 이름이 출장 정보에 없다면 %s 이름 추가.
            프롬프트 요청자로 예상되면 %s 이름 추가.
            문장 :
            %s""",
        member.getName(),
        member.getName(),
        request.prompt()
    );

    return chatClient.prompt()
        .user(p -> p.text(fullPrompt))
        .call()
        .entity(HotelList.class);
  }

  public FlightList extractFlightInfo(Long memberId, PromptRequest request) {
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(MemberNotFoundException::new);

    String fullPrompt = String.format(
        """
            다음 문장에서 출장 정보를 추출해줘. 올해는 2025년이야. 예산은 숙박 일수 만큼 나눠.
            출발지는 depart_date 에 저장하고 도착지은 return_date 에 저장해.
            문장을 파악해서 요청자와 같이 출장을 가는 사람 이름이면 그것에 맞춰 인원 수 추가.
            만약 9월 23일부터 4박 5일이면 depart_date = 2025-09-23, return_date = 2025-09-27.
            출장 정보 한 개당 최소 한 명의 이름 필요.
            만약 이름이 출장 정보에 없다면 %s 이름 추가.
            프롬프트 요청자로 예상되면 %s 이름 추가.
            문장 :
            %s""",
        member.getName(),
        member.getName(),
        request.prompt()
    );
    return chatClient.prompt()
        .user(p -> p.text(fullPrompt))
        .call()
        .entity(FlightList.class);
  }

  private MemberInfoList extractNames(Long memberId, PromptRequest request) {
    String fullPrompt = """
        다음 문장에서 사람 이름을 추출해줘. 만약 프롬프트 요청한 사람이 있으면 '요청자' 라고 저장해줘.
        사람 이름이 없으면 '요청자' 라고 저장해줘.
        문장 :
        """ + request.prompt();

    NameList nameList = chatClient.prompt()
        .user(p -> p.text(fullPrompt))
        .call()
        .entity(NameList.class);

    return extractMemberInfo(memberId, nameList);
  }

  private MemberInfoList extractMemberInfo(Long memberId, NameList nameList) {
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(MemberNotFoundException::new);
    MemberInfoGetResponse memberInfoGetResponse = Member.toMemberInfoGetResponse(member, kmsUtil);

    Company company = member.getCompany();

    List<MemberInfoGetResponse> resultList = new ArrayList<>();

    if (nameList.names().contains("요청자")) {
      resultList.add(memberInfoGetResponse);
      nameList.names().remove("요청자");
    }
    for (String name : nameList.names()) {

      Member findMember = memberRepository.findByNameAndCompany(name, company)
          .orElseThrow(MemberNotFoundException::new);
      System.out.println("name :" + findMember.getName());
      MemberInfoGetResponse findMemberInfo = Member.toMemberInfoGetResponse(findMember, kmsUtil);
      resultList.add(findMemberInfo);
    }

    return new MemberInfoList(resultList);
  }

}