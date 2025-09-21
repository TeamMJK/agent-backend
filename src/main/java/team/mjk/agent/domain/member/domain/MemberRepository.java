package team.mjk.agent.domain.member.domain;

import java.util.List;
import java.util.Optional;
import team.mjk.agent.domain.company.domain.Company;

public interface MemberRepository {

    Member save(Member member);

    boolean existsByEmail(String email);

    Member findByMemberId(Long memberId);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNameAndCompany(String name, Company company);

    List<Member> findAllByCompanyId(Long companyId);

    void delete(Long memberId);

    long countByCompanyId(Long companyId);

    boolean existsByCompanyId(Long companyId);

}
