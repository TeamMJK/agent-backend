package team.mjk.agent.domain.businessTrip;



import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import team.mjk.agent.domain.businessTrip.application.BusinessTripService;
import team.mjk.agent.domain.businessTrip.domain.BusinessTrip;
import team.mjk.agent.domain.businessTrip.domain.BusinessTripRepository;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripSaveRequest;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripUpdateRequest;
import team.mjk.agent.domain.businessTrip.dto.response.BusinessTripGetAllResponse;
import team.mjk.agent.domain.businessTrip.dto.response.BusinessTripGetResponse;
import team.mjk.agent.domain.businessTrip.dto.response.BusinessTripSaveResponse;
import team.mjk.agent.domain.businessTrip.dto.response.BusinessTripUpdateResponse;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.member.presentation.exception.MemberNotFoundException;

@ExtendWith(MockitoExtension.class)
class BusinessTripServiceTest {

  @Mock
  private MemberRepository memberRepository;
  @Mock
  private BusinessTripRepository businessTripRepository;

  @InjectMocks
  private BusinessTripService businessTripService;

  private Member member;
  private Company company;

  @BeforeEach
  void setUp() {
    company = mock(Company.class);
    when(company.getId()).thenReturn(1L);

    member = mock(Member.class);
    when(member.getCompany()).thenReturn(company);
    when(member.getName()).thenReturn("홍길동");
  }

  @Test
  @DisplayName("출장 저장 성공")
  void save_success() {
    // given
    BusinessTripSaveRequest request = mock(BusinessTripSaveRequest.class);
    when(request.departDate()).thenReturn(LocalDate.of(2025,7,10));
    when(request.arriveDate()).thenReturn(LocalDate.of(2025, 7, 12));
    when(request.destination()).thenReturn("부산");
    when(request.names()).thenReturn(Collections.singletonList("홍길동,김철수"));

    when(memberRepository.findByMemberId(1L)).thenReturn(Optional.of(member));

    BusinessTrip businessTrip = mock(BusinessTrip.class);
    when(businessTrip.getId()).thenReturn(100L);

    // BusinessTrip.create(...)가 static이면 PowerMockito 등 추가 도구 필요
    // 여기서는 단순화하여 new로 가정
    when(businessTripRepository.save(any(BusinessTrip.class))).thenReturn(businessTrip);

    // when
    BusinessTripSaveResponse response = businessTripService.save(1L, request);

    // then
    assertThat(response.businessTripId()).isEqualTo(100L);
    verify(businessTripRepository).save(any(BusinessTrip.class));
  }

  @Test
  @DisplayName("출장 저장 실패 - 없는 멤버")
  void save_fail_memberNotFound() {
    when(memberRepository.findByMemberId(2L)).thenReturn(Optional.empty());
    BusinessTripSaveRequest request = mock(BusinessTripSaveRequest.class);

    assertThatThrownBy(() -> businessTripService.save(2L, request))
        .isInstanceOf(MemberNotFoundException.class);
  }

  @Test
  @DisplayName("출장 단건 조회 성공")
  void getBusinessTrip_success() {
    when(memberRepository.findByMemberId(1L)).thenReturn(Optional.of(member));
    BusinessTrip businessTrip = mock(BusinessTrip.class);
    when(businessTrip.getDepartDate()).thenReturn(LocalDate.of(2025,7,10));
    when(businessTrip.getArriveDate()).thenReturn(LocalDate.of(2025, 7, 12));
    when(businessTrip.getDestination()).thenReturn("부산");
    when(businessTrip.getNames()).thenReturn(Collections.singletonList("홍길동,김철수"));
    when(businessTrip.getWriter()).thenReturn("홍길동");

    when(businessTripRepository.findByIdAndCompanyId(10L, 1L)).thenReturn(businessTrip);

    BusinessTripGetResponse response = businessTripService.getBusinessTrip(1L, 10L);

    assertThat(response.departDate()).isEqualTo("2025-07-10");
    assertThat(response.arriveDate()).isEqualTo("2025-07-12");
    assertThat(response.destination()).isEqualTo("부산");
    assertThat(response.names()).isEqualTo("홍길동,김철수");
    assertThat(response.writer()).isEqualTo("홍길동");
  }

  @Test
  @DisplayName("출장 전체 조회")
  void getAllBusinessTrip_success() {
    when(memberRepository.findByMemberId(1L)).thenReturn(Optional.of(member));
    BusinessTrip businessTrip = mock(BusinessTrip.class);
    List<BusinessTrip> businessTrips = List.of(businessTrip);

    when(businessTripRepository.findAllByCompanyId(1L)).thenReturn(businessTrips);

    BusinessTripGetAllResponse response = businessTripService.getAllBusinessTrip(1L);

    assertThat(response.businessTripList()).hasSize(1);
  }

  @Test
  @DisplayName("출장 수정 성공")
  void update_success() {
    when(memberRepository.findByMemberId(1L)).thenReturn(Optional.of(member));
    BusinessTrip businessTrip = mock(BusinessTrip.class);
    when(businessTripRepository.findByIdAndCompanyId(10L, 1L)).thenReturn(businessTrip);

    BusinessTripUpdateRequest request = mock(BusinessTripUpdateRequest.class);

    BusinessTripUpdateResponse response = businessTripService.update(1L, 10L, request);

    verify(businessTrip).update(request);
    assertThat(response.businessTripId()).isEqualTo(10L);
  }
}
