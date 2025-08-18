package team.mjk.agent.domain.prompt.application;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.member.dto.response.MemberInfoGetResponse;
import team.mjk.agent.domain.member.dto.response.MemberInfoList;
import team.mjk.agent.domain.member.presentation.exception.MemberNotFoundException;
import team.mjk.agent.domain.prompt.dto.request.PromptRequest;
import team.mjk.agent.domain.prompt.dto.response.BusinessTripAndMemberInfoResponse;
import team.mjk.agent.domain.prompt.dto.response.BusinessTripList;
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

  public HotelAndMemberInfoResponse extractHotel(Long memberId, PromptRequest request) {
    HotelList hotelList = extractHotelInfo(memberId, request);
    MemberInfoList memberInfoList = extractNames(memberId, request);

    return new HotelAndMemberInfoResponse(hotelList, memberInfoList);
  }

  public BusinessTripAndMemberInfoResponse extractBusinessTrip(Long memberId,
      PromptRequest request) {
    BusinessTripList businessTripList = extractBusinessTripInfo(memberId, request);
    MemberInfoList memberInfoList = extractNames(memberId, request);

    return new BusinessTripAndMemberInfoResponse(businessTripList, memberInfoList);
  }

  public IntegrationResponse extractIntegration(Long memberId, PromptRequest request) {
    BusinessTripList businessTripList = extractBusinessTripInfo(memberId, request);
    HotelList hotelList = extractHotelInfo(memberId, request);
    MemberInfoList memberInfoList = extractNames(memberId, request);

    return new IntegrationResponse(businessTripList, hotelList, memberInfoList);
  }

  public HotelList extractHotelInfo(Long memberId, PromptRequest request) {
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(MemberNotFoundException::new);

    String fullPrompt = String.format(
        "다음 문장에서 출장 정보를 추출해줘. 올해는 2025년이야. 예산은 숙박 일수 만큼 나눠.\n" +
            "출발일은 departure_date 에 저장하고 도착일은 arrival_date 에 저장해.\n" +
            "문장을 파악해서 요청자와 같이 출장을 가는 사람 이름이면 그것에 맞춰 인원 수 추가.\n" +
            "출장 정보 한 개당 최소 한 명의 이름 필요.\n" +
            "만약 이름이 출장 정보에 없다면 %s 이름 추가.\n" +
            "프롬프트 요청자로 예상되면 %s 이름 추가.\n" +
            "문장 :\n%s",
        member.getName(),
        member.getName(),
        request.prompt()
    );

    return chatClient.prompt()
        .user(p -> p.text(fullPrompt))
        .call()
        .entity(HotelList.class);
  }

  public BusinessTripList extractBusinessTripInfo(Long memberId, PromptRequest request) {
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(MemberNotFoundException::new);

    String fullPrompt = String.format(
        "다음 문장에서 출장 정보를 추출해줘. 올해는 2025년이야. 예산은 숙박 일수 만큼 나눠.\n" +
            "출발지는 depart_date 에 저장하고 도착지은 return_date 에 저장해.\n" +
            "문장을 파악해서 요청자와 같이 출장을 가는 사람 이름이면 그것에 맞춰 인원 수 추가.\n" +
            "출장 정보 한 개당 최소 한 명의 이름 필요.\n" +
            "만약 이름이 출장 정보에 없다면 %s 이름 추가.\n" +
            "프롬프트 요청자로 예상되면 %s 이름 추가.\n" +
            "문장 :\n%s",
        member.getName(),
        member.getName(),
        request.prompt()
    );
    return chatClient.prompt()
        .user(p -> p.text(fullPrompt))
        .call()
        .entity(BusinessTripList.class);
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