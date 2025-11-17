package team.mjk.agent.domain.member.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.member.application.dto.response.MemberInfoGetResponse;

public interface MemberRepository {

    Member save(Member member);

    boolean existsByEmail(String email);

    Member findByMemberId(Long memberId);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNameAndCompany(String name, Company company);

    List<Member> findAllByCompanyId(Long companyId);

    List<MemberInfoGetResponse> findAllMemberInfoByCompanyId(Long companyId);

    void delete(Long memberId);

    long countByCompanyId(Long companyId);

    void deleteAllByCompanyId(Long companyId);

    void nullifyMembersCompanyByCompanyId(Long companyId);

}
