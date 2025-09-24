package team.mjk.agent.domain.vnc.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.mjk.agent.domain.member.domain.Member;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class Vnc {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(columnDefinition = "TEXT")
  private String sessionId;

  @Column(columnDefinition = "TEXT")
  private String vncUrl;

  private VncStatus vncStatus;

  private String hotelDestination;

  private String bookingDates;

  private int guests;

  private int budget;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @Builder
  private Vnc(
      String sessionId,
      String vncUrl,
      VncStatus vncStatus,
      Member member,
      String hotelDestination,
      String bookingDates,
      int guests,
      int budget
  ) {
    this.sessionId = sessionId;
    this.vncUrl = vncUrl;
    this.vncStatus = vncStatus;
    this.member = member;
    this.hotelDestination = hotelDestination;
    this.bookingDates = bookingDates;
    this.guests = guests;
    this.budget = budget;
  }

  public static Vnc create(
      String sessionId,
      String vncUrl,
      VncStatus vncStatus,
      Member member,
      String hotelDestination,
      String bookingDates,
      int guests,
      int budget
      ) {
    return Vnc.builder()
        .sessionId(sessionId)
        .vncUrl(vncUrl)
        .vncStatus(vncStatus)
        .member(member)
        .hotelDestination(hotelDestination)
        .bookingDates(bookingDates)
        .guests(guests)
        .budget(budget)
        .build();
  }

  public void updateStatus(VncStatus vncStatus) {
    this.vncStatus = vncStatus;
  }

}