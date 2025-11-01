package team.mjk.agent.domain.prompt.application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import team.mjk.agent.domain.agoda.dto.request.AgodaHotelInfo;
import team.mjk.agent.domain.agoda.dto.response.HotelResult;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.flight.application.FlightService;
import team.mjk.agent.domain.flight.dto.FlightAndMemberInfoResponse;
import team.mjk.agent.domain.flight.dto.FlightList;
import team.mjk.agent.domain.hotel.application.HotelService;
import team.mjk.agent.domain.hotel.dto.AgodaHotelList;
import team.mjk.agent.domain.hotel.dto.HotelAndMemberInfoResponse;
import team.mjk.agent.domain.hotel.dto.HotelList;
import team.mjk.agent.domain.passport.domain.Passport;
import team.mjk.agent.domain.passport.domain.PassportRepository;
import team.mjk.agent.domain.vnc.dto.VncResponseList;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.member.application.dto.response.MemberInfoGetResponse;
import team.mjk.agent.domain.member.application.dto.response.MemberInfoList;
import team.mjk.agent.domain.member.presentation.exception.MemberNotFoundException;
import team.mjk.agent.domain.prompt.dto.request.PromptRequest;
import team.mjk.agent.domain.prompt.dto.response.NameList;
import team.mjk.agent.domain.prompt.presentation.exception.EmptyArrivalDateException;
import team.mjk.agent.domain.prompt.presentation.exception.EmptyDepartureDateExceptionCode;
import team.mjk.agent.domain.prompt.presentation.exception.EmptyDepartureException;
import team.mjk.agent.domain.prompt.presentation.exception.EmptyDestinationException;
import team.mjk.agent.global.util.KmsUtil;

@Slf4j
@RequiredArgsConstructor
@Service
public class PromptService {

  private final ChatClient chatClient;
  private final MemberRepository memberRepository;
  private final KmsUtil kmsUtil;
  private final FlightService flightService;
  private final HotelService hotelService;
  private final PassportRepository passportRepository;

  public void handleIntegration(Long memberId, PromptRequest request) throws JsonProcessingException {
    extractFlight(memberId, request);
    extractHotel(memberId, request);
  }

  public VncResponseList extractHotel(Long memberId, PromptRequest request) throws JsonProcessingException {
    HotelList hotelList = extractHotelInfo(memberId, request);
    MemberInfoList memberInfoList = extractNames(memberId, request);

    HotelAndMemberInfoResponse response = new HotelAndMemberInfoResponse(hotelList, memberInfoList);

    VncResponseList result = hotelService.getHotel(memberId, response);
    hotelService.handleHotel(memberId, response, result);
    return result;
  }

  public void extractFlight(Long memberId,
      PromptRequest request) {
    FlightList flightList = extractFlightInfo(memberId, request);
    MemberInfoList memberInfoList = extractNames(memberId, request);

    FlightAndMemberInfoResponse response = new FlightAndMemberInfoResponse(flightList,
        memberInfoList);

    flightService.handleFlight(memberId, response);
  }

  public AgodaHotelList extractAgodaHotelInfo(Long memberId, PromptRequest request) {
    Member member = memberRepository.findByMemberId(memberId);

    String fullPrompt = String.format(
        """
            너는 숙박 예약해주는 AI야. 모든 문장을 숙박 시설 예약하는 것으로 간주하고 생각해
            다음 문장에서 출장 정보를 추출해줘. 올해는 2025년이야. 예산은 숙박 일수 만큼 나눠.
            날짜 중 더 빠른 날짜를 departure_date 에 저장해.
            월을 못찾겠으면 앞선 날짜의 월로 추측해.
            예)
            11월3일부터 5일 까지 인천으로 가. 예산은 20만원 -> arrival_date = 2025-11-05
            출발일은 departure_date 에 저장하고 도착일은 arrival_date 에 저장해.
            문장을 파악해서 요청자와 같이 출장을 가는 사람 이름이면 그것에 맞춰 인원 수 추가.
            만약 9월 23일부터 4박 5일이면 depart_date = 2025-09-23, return_date = 2025-09-27.
            만약 "11월3일부터 5일 까지 안동으로 가. 예산은 15만원이여" 일 때 maximum 은 150000
            만약 문장에 '다음주 월요일'과 같이 상대적인 날짜가 나오면, 2025년 기준으로 프롬프트 요청 날짜 기준으로 절대 날짜로 변환해 departure_date와 arrival_date에 넣어줘.
            문장 :
            %s""",
        request.prompt()
    );

    AgodaHotelList hotelList = chatClient.prompt()
        .user(p -> p.text(fullPrompt))
        .call()
        .entity(AgodaHotelList.class);

    hotelList.agodaHotelList().forEach(hotel -> {
      if (hotel.destination() == null || hotel.destination().isBlank()) {
        throw new EmptyDestinationException();
      }
      if (hotel.departure_date() == null) {
        throw new EmptyDepartureDateExceptionCode();
      }
      if (hotel.arrival_date() == null) {
        throw new EmptyArrivalDateException();
      }
    });

    return hotelList;
  }

  public HotelList extractHotelInfo(Long memberId, PromptRequest request) {
    Member member = memberRepository.findByMemberId(memberId);

    String fullPrompt = String.format(
        """
            다음 문장에서 출장 정보를 추출해줘. 올해는 2025년이야. 예산은 숙박 일수 만큼 나눠.
            날짜 중 더 빠른 날짜를 departure_date 에 저장해.
            월을 못찾겠으면 앞선 날짜의 월로 추측해.
            예)
            11월3일부터 5일 까지 인천으로 가. 예산은 20만원 -> arrival_date = 2025-11-05
            출발일은 departure_date 에 저장하고 도착일은 arrival_date 에 저장해.
            문장을 파악해서 요청자와 같이 출장을 가는 사람 이름이면 그것에 맞춰 인원 수 추가.
            만약 9월 23일부터 4박 5일이면 depart_date = 2025-09-23, return_date = 2025-09-27.
            출장 정보 한 개당 최소 한 명의 이름 필요.
            만약 이름이 출장 정보에 없다면 %s 이름 추가.
            사용자의 목적지에 행정 구역이 있으면 항상 도시로 변환해.
            프롬프트 요청자로 예상되면 %s 이름 추가.
            문장 :
            %s""",
        member.getName(),
        member.getName(),
        request.prompt()
    );

    HotelList hotelList = chatClient.prompt()
        .user(p -> p.text(fullPrompt))
        .call()
        .entity(HotelList.class);

    hotelList.hotels().forEach(hotel -> {
      if (hotel.destination() == null || hotel.destination().isBlank()) {
        throw new EmptyDestinationException();
      }
      if (hotel.departure_date() == null) {
        throw new EmptyDepartureDateExceptionCode();
      }
      if (hotel.arrival_date() == null) {
        throw new EmptyArrivalDateException();
      }
    });

    return hotelList;
  }

  private FlightList extractFlightInfo(Long memberId, PromptRequest request) {
    Member member = memberRepository.findByMemberId(memberId);

    String fullPrompt = String.format(
        """
            다음 문장에서 출장 정보를 추출해줘. 올해는 2025년이야. 예산은 숙박 일수 만큼 나눠.
            출발지는 depart_date 에 저장하고 도착지은 return_date 에 저장해.
            문장을 파악해서 요청자와 같이 출장을 가는 사람 이름이면 그것에 맞춰 인원 수 추가.
            출발지랑 도착지는 항공으로 검색 가능하게 저장.
            만약 도시 이름이라면 가장 가까운 항공으로 저장.
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
    FlightList flightList = chatClient.prompt()
        .user(p -> p.text(fullPrompt))
        .call()
        .entity(FlightList.class);

    flightList.flights().forEach(flight -> {
      if (flight.departure() == null || flight.departure().isBlank()) {
        throw new EmptyDepartureException();
      }
      if (flight.arrival() == null || flight.arrival().isBlank()) {
        throw new EmptyDestinationException();
      }
      if (flight.depart_date() == null) {
        throw new EmptyDepartureDateExceptionCode();
      }
      if (flight.return_date() == null) {
        throw new EmptyArrivalDateException();
      }
    });

    return flightList;
  }

  public AgodaHotelInfo extractAgodaHotelInfo(HotelResult hotelResult) {
    String fullPrompt = String.format("""
        넌 관광 및 호텔 평가 전문가야. 모두 한글로 설명해.
        실시간으로 호텔 정보를 토대로 장단점 및 주변 관광지 추천 및 맛집을 찾아주고 한글로 알려줘.
        관광지 추천을 할 땐 숙소에서 얼마나 걸리는지도 알려줘.
        맛집을 추천할 땐 네이버에서 현재 존재하는 식당인지 검증하고 추천해줘.
        아래 모든 정보가 확인 가능한 식당만 추천해줘.
        아침 식사 시간이랑 브레이크 타임이랑 주문 마감 시간이랑 휴일 같은 것도 검증된 정보를 찾아서 알려줘.
        - Advantages
        - Disadvantages
        - Nearby tourist attractions
        - Recommended restaurants

        Hotel: %s
        """, hotelResult.hotelName());

    return chatClient.prompt()
        .user(p -> p.text(fullPrompt))
        .call()
        .entity(AgodaHotelInfo.class);
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
    Member member = memberRepository.findByMemberId(memberId);
    MemberInfoGetResponse memberInfoGetResponse = MemberInfoGetResponse.from(member, kmsUtil);
    Company company = member.getValidatedCompany();

    List<MemberInfoGetResponse> resultList = new ArrayList<>();

    if (nameList.names().contains("요청자")) {
      resultList.add(memberInfoGetResponse);
      nameList.names().remove("요청자");
    }
    for (String name : nameList.names()) {

      Member findMember = memberRepository.findByNameAndCompany(name, company)
          .orElseThrow(MemberNotFoundException::new);
      System.out.println("name :" + findMember.getName());
      MemberInfoGetResponse findMemberInfo = MemberInfoGetResponse.from(findMember, kmsUtil);
      resultList.add(findMemberInfo);
    }

    return new MemberInfoList(resultList);
  }

}