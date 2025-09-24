package team.mjk.agent.domain.vnc.application;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import team.mjk.agent.domain.hotel.dto.VncBusinessInfo;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.vnc.domain.Vnc;
import team.mjk.agent.domain.vnc.domain.VncRepository;
import team.mjk.agent.domain.vnc.domain.VncStatus;
import team.mjk.agent.domain.vnc.dto.VncResponse;
import team.mjk.agent.domain.vnc.dto.VncResponseList;
import team.mjk.agent.domain.vnc.dto.VncSessionIdRequest;
import team.mjk.agent.global.util.AgentResponseUtil;

@RequiredArgsConstructor
@Service
public class VncService {

  private final VncRepository vncRepository;
  private final MemberRepository memberRepository;
  private final AgentResponseUtil agentResponseUtil;

  public List<VncResponse> getVncList(Long memberId) {
    Member member = memberRepository.findByMemberId(memberId);

    return getVncResponses(member);
  }

  @Transactional
  public List<VncResponse> pause(Long memberId, VncSessionIdRequest request) {
    Member member = memberRepository.findByMemberId(memberId);
    pauseVnc(memberId,request);

    agentResponseUtil.pauseAgent(request.sessionId());
    return getVncResponses(member);
  }

  @NotNull
  private List<VncResponse> getVncResponses(Member member) {
    List<Vnc> vncList = vncRepository.findAllByMember(member);

    return vncList.stream()
        .map(vnc -> {
          // VncBusinessInfo 생성
          VncBusinessInfo businessInfo = new VncBusinessInfo(
              vnc.getHotelDestination(),
              vnc.getBookingDates(),
              vnc.getGuests(),
              vnc.getBudget()
          );

          // VncResponse 생성
          return VncResponse.builder()
              .session_id(vnc.getSessionId())
              .novnc_url(vnc.getVncUrl())
              .vncBusinessInfo(businessInfo)
              .status(vnc.getVncStatus())
              .build();
        })
        .toList();
  }

  @Transactional
  public void pauseVnc(Long memberId, VncSessionIdRequest request) {
    Member member = memberRepository.findByMemberId(memberId);

    Vnc vnc = vncRepository.findByMemberAndSessionId(member,request.sessionId());
    vnc.updateStatus(VncStatus.PAUSE);
  }

}