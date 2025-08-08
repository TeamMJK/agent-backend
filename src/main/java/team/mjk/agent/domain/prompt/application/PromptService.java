package team.mjk.agent.domain.prompt.application;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.member.dto.response.MemberInfoGetResponse;
import team.mjk.agent.domain.member.dto.response.MemberInfoList;
import team.mjk.agent.domain.member.presentation.exception.MemberNotFoundException;
import team.mjk.agent.domain.prompt.dto.request.PromptRequest;
import team.mjk.agent.domain.prompt.dto.response.HotelAndMemberInfoResponse;
import team.mjk.agent.domain.prompt.dto.response.HotelList;
import team.mjk.agent.domain.prompt.dto.response.NameList;
import team.mjk.agent.global.util.KmsUtil;

@RequiredArgsConstructor
@Service
public class PromptService {

  private final ChatClient chatClient;
  private final MemberRepository memberRepository;
  private final KmsUtil kmsUtil;

  @Autowired
  public PromptService(Builder chatClientBuilder, MemberRepository memberRepository,
      KmsUtil kmsUtil) {
    this.chatClient = chatClientBuilder.build();
    this.memberRepository = memberRepository;
    this.kmsUtil = kmsUtil;
  }

  public HotelAndMemberInfoResponse extract(Long memberId, PromptRequest request) {
    HotelList hotelList = extractHotel(request);
    MemberInfoList memberInfoList = extractNames(memberId,request);
    return new HotelAndMemberInfoResponse(hotelList,memberInfoList);
  }

  public HotelList extractHotel(PromptRequest request) {
    String fullPrompt = """
        다음 문장에서 출장 정보를 추출해줘. 올해는 2025년이야. 예산은 숙박 일수 만큼 나눠.
        문장을 파악해서 요청자와 같이 출장을 가는 사람 이름이면 그것에 맞춰 인원 수 추가
        문장 :
        """ + request.prompt();

    return chatClient.prompt()
        .user(p->p.text(fullPrompt))
        .call()
        .entity(HotelList.class);
  }

  public MemberInfoList extractNames(Long memberId,PromptRequest request) {
    String fullPrompt = """
        다음 문장에서 사람 이름을 추출해줘. 만약 프롬프트 요청한 사람이 있으면 '요청자' 라고 저장해줘.
        문장 :
        """ + request.prompt();

    NameList nameList = chatClient.prompt()
        .user(p->p.text(fullPrompt))
        .call()
        .entity(NameList.class);

    return extractMemberInfo(memberId,nameList);
  }

  public MemberInfoList extractMemberInfo(Long memberId, NameList nameList) {
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(MemberNotFoundException::new);
    MemberInfoGetResponse memberInfoGetResponse = Member.toMemberInfoGetResponse(member,kmsUtil);

    Company company = member.getCompany();

    List<MemberInfoGetResponse> resultList = new ArrayList<>();

    if(nameList.names().contains("요청자")) {
      resultList.add(memberInfoGetResponse);
      nameList.names().remove("요청자");
    }
    for(String name : nameList.names()) {

      Member findMember = memberRepository.findByNameAndCompany(name,company)
          .orElseThrow(MemberNotFoundException::new);
      System.out.println("name :" + findMember.getName());
      MemberInfoGetResponse findMemberInfo = Member.toMemberInfoGetResponse(findMember,kmsUtil);
      resultList.add(findMemberInfo);
    }

    return new MemberInfoList(resultList);
  }

}