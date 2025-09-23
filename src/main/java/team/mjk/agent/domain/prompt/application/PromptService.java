package team.mjk.agent.domain.prompt.application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.company.domain.CompanyRepository;
import team.mjk.agent.domain.flight.application.FlightService;
import team.mjk.agent.domain.flight.dto.FlightAndMemberInfoResponse;
import team.mjk.agent.domain.flight.dto.FlightList;
import team.mjk.agent.domain.hotel.application.HotelService;
import team.mjk.agent.domain.hotel.dto.HotelAndMemberInfoResponse;
import team.mjk.agent.domain.hotel.dto.HotelList;
import team.mjk.agent.domain.hotel.dto.SessionIdAndVncList;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.member.dto.response.MemberInfoGetResponse;
import team.mjk.agent.domain.member.dto.response.MemberInfoList;
import team.mjk.agent.domain.member.presentation.exception.MemberNotFoundException;
import team.mjk.agent.domain.prompt.dto.request.PromptRequest;
import team.mjk.agent.domain.prompt.dto.response.NameList;
import team.mjk.agent.domain.prompt.presentation.exception.EmptyArrivalDateException;
import team.mjk.agent.domain.prompt.presentation.exception.EmptyDepartureDateExceptionCode;
import team.mjk.agent.domain.prompt.presentation.exception.EmptyDepartureException;
import team.mjk.agent.domain.prompt.presentation.exception.EmptyDestinationException;
import team.mjk.agent.global.util.KmsUtil;

@RequiredArgsConstructor
@Service
public class PromptService {

  private final ChatClient chatClient;
  private final MemberRepository memberRepository;
  private final KmsUtil kmsUtil;
  private final FlightService flightService;
  private final HotelService hotelService;

  public void handleIntegration(Long memberId, PromptRequest request) {
    extractFlight(memberId, request);
    extractHotel(memberId, request);
  }

  public SessionIdAndVncList extractHotel(Long memberId, PromptRequest request) {
    HotelList hotelList = extractHotelInfo(memberId, request);
    MemberInfoList memberInfoList = extractNames(memberId, request);

    HotelAndMemberInfoResponse response = new HotelAndMemberInfoResponse(hotelList, memberInfoList);
    SessionIdAndVncList result = hotelService.getHotel(memberId,response);
    //hotelService.handleHotel(memberId,response,result);
    return  result;
  }

  public void extractFlight(Long memberId,
      PromptRequest request) {
    FlightList flightList = extractFlightInfo(memberId, request);
    MemberInfoList memberInfoList = extractNames(memberId, request);

    FlightAndMemberInfoResponse response = new FlightAndMemberInfoResponse(flightList, memberInfoList);

    flightService.handleFlight(memberId,response);
  }

  private HotelList extractHotelInfo(Long memberId, PromptRequest request) {
    Member member = memberRepository.findByMemberId(memberId);

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
      if(flight.arrival() == null || flight.arrival().isBlank()) {
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