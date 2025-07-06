package team.mjk.agent.domain.company.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.company.domain.CompanyRepository;
import team.mjk.agent.domain.company.dto.request.CompanyInvitationCodeRequest;
import team.mjk.agent.domain.company.dto.request.CompanySaveRequest;
import team.mjk.agent.domain.invitation.InvitationCodeProvider;
import team.mjk.agent.domain.invitation.domain.Invitation;
import team.mjk.agent.domain.invitation.domain.InvitationRepository;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.member.presentation.exception.MemberNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompanyServiceTest {

  @Mock
  private InvitationCodeProvider invitationCodeProvider;
  @Mock
  private InvitationRepository invitationRepository;
  @Mock
  private CompanyRepository companyRepository;
  @Mock
  private MemberRepository memberRepository;

  @InjectMocks
  private CompanyService companyService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @DisplayName("초대 코드 생성 성공")
  @Test
  void createInvitationCode_success() {
    // given
    Long companyId = 1L;
    when(companyRepository.findById(companyId)).thenReturn(Optional.of(new Company("회사", "대표")));
    Invitation invitation = mock(Invitation.class);
    when(invitationCodeProvider.create(companyId)).thenReturn(invitation);

    // when
    Invitation result = companyService.createInvitationCode(companyId);

    // then
    assertThat(result).isNotNull();
    verify(invitationCodeProvider).create(companyId);
  }

  @DisplayName("초대 코드 생성 실패 - 회사 없음")
  @Test
  void createInvitationCode_fail_companyNotFound() {
    // given
    Long companyId = 2L;
    when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> companyService.createInvitationCode(companyId))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Company not found");
  }

  @DisplayName("회사 생성 성공")
  @Test
  void create_success() {
    // given
    Long memberId = 1L;
    String companyName = "테스트회사";
    String memberName = "홍길동";
    CompanySaveRequest request = new CompanySaveRequest(companyName);

    Member member = mock(Member.class);
    when(member.getName()).thenReturn(memberName);
    when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.of(member));

    Company company = new Company(companyName, memberName);
    when(companyRepository.save(any(Company.class))).thenReturn(company);

    // when
    Long companyId = companyService.create(request, memberId);

    // then
    assertThat(companyId).isEqualTo(company.getId());
    verify(companyRepository).save(any(Company.class));
  }

  @DisplayName("회사 생성 실패 - 멤버 없음")
  @Test
  void create_fail_memberNotFound() {
    // given
    Long memberId = 1L;
    CompanySaveRequest request = new CompanySaveRequest("테스트회사");
    when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> companyService.create(request, memberId))
        .isInstanceOf(MemberNotFoundException.class);
  }

  @DisplayName("초대 코드로 회사 가입 성공")
  @Test
  void join_success() {
    // given
    Long memberId = 1L;
    String code = "INVITE123";
    Long companyId = 2L;
    String companyName = "테스트회사";

    Member member = mock(Member.class);
    when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.of(member));

    Invitation invitation = mock(Invitation.class);
    when(invitation.getCompanyId()).thenReturn(companyId);
    when(invitationRepository.findByCode(code)).thenReturn(Optional.of(invitation));

    Company company = mock(Company.class);
    when(company.getName()).thenReturn(companyName);
    when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));

    // when
    String result = companyService.join(memberId, new CompanyInvitationCodeRequest(code));

    // then
    assertThat(result).isEqualTo(companyName);
    verify(member).saveCompany(company);
    verify(memberRepository).save(member);
  }

  @DisplayName("초대 코드로 회사 가입 실패 - 멤버 없음")
  @Test
  void join_fail_memberNotFound() {
    // given
    Long memberId = 1L;
    String code = "INVITE123";
    when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> companyService.join(memberId, new CompanyInvitationCodeRequest(code)))
        .isInstanceOf(MemberNotFoundException.class);
  }

  @DisplayName("초대 코드로 회사 가입 실패 - 초대 코드 없음")
  @Test
  void join_fail_invitationNotFound() {
    // given
    Long memberId = 1L;
    String code = "INVITE123";
    Member member = mock(Member.class);
    when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.of(member));
    when(invitationRepository.findByCode(code)).thenReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> companyService.join(memberId, new CompanyInvitationCodeRequest(code)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("유효하지 않은 초대 코드");
  }

  @DisplayName("초대 코드로 회사 가입 실패 - 회사 없음")
  @Test
  void join_fail_companyNotFound() {
    // given
    Long memberId = 1L;
    String code = "INVITE123";
    Long companyId = 2L;
    Member member = mock(Member.class);
    Invitation invitation = mock(Invitation.class);
    when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.of(member));
    when(invitationRepository.findByCode(code)).thenReturn(Optional.of(invitation));
    when(invitation.getCompanyId()).thenReturn(companyId);
    when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> companyService.join(memberId, new CompanyInvitationCodeRequest(code)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("존재하지 않은 회사");
  }

}