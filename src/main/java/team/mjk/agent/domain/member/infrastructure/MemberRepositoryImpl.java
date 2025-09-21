package team.mjk.agent.domain.member.infrastructure;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;

import java.util.Optional;
import team.mjk.agent.domain.member.presentation.exception.MemberNotFoundException;

@RequiredArgsConstructor
@Repository
public class MemberRepositoryImpl implements MemberRepository {

  private final MemberJpaRepository memberJpaRepository;

  @Override
  public Member save(Member member) {
    return memberJpaRepository.save(member);
  }

  @Override
  public boolean existsByEmail(String email) {
    return memberJpaRepository.existsByEmail(email);
  }

  @Override
  public Member findByMemberId(Long memberId) {
    return memberJpaRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);
  }

  @Override
  public Optional<Member> findByEmail(String email) {
    return memberJpaRepository.findByEmail(email);
  }

  @Override
  public Optional<Member> findByNameAndCompany(String name, Company company) {
    return memberJpaRepository.findByNameAndCompany(name, company);
  }

  @Override
  public List<Member> findAllByCompanyId(Long companyId) {
    return memberJpaRepository.findAllByCompanyId(companyId);
  }

  @Override
  public void delete(Long memberId) {
    memberJpaRepository.deleteById(memberId);
  }

  @Override
  public long countByCompanyId(Long companyId) {
    return memberJpaRepository.countByCompanyId(companyId);
  }

  @Override
  public void deleteAllByCompanyId(Long companyId) {
    memberJpaRepository.deleteAllByCompanyId(companyId);
  }

}