package team.mjk.agent.domain.member.infrastructure;

import io.jsonwebtoken.security.Jwks.OP;
import java.util.List;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder.Op;
import org.springframework.data.jpa.repository.JpaRepository;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.member.domain.Member;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNameAndCompany(String name, Company company);

    Optional<List<Member>> findAllByCompanyId(Long companyId);

    long countByCompanyId(Long companyId);

}
