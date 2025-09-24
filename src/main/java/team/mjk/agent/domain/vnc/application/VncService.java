package team.mjk.agent.domain.vnc.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.mjk.agent.domain.hotel.dto.VncBusinessInfo;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.vnc.domain.Vnc;
import team.mjk.agent.domain.vnc.domain.VncRepository;
import team.mjk.agent.domain.vnc.dto.VncResponse;
import team.mjk.agent.domain.vnc.dto.VncResponseList;

@RequiredArgsConstructor
@Service
public class VncService {

  private final VncRepository vncRepository;
  private final MemberRepository memberRepository;

  public List<VncResponse> getVncList(Long memberId) {
    Member member = memberRepository.findByMemberId(memberId);

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

}