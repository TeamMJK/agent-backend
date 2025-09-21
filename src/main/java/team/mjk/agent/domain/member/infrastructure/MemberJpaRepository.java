package team.mjk.agent.domain.member.infrastructure;

import io.jsonwebtoken.security.Jwks.OP;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder.Op;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.member.domain.Member;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    @Query("select m from Member m join fetch m.company left join fetch m.passport where m.name = :name and m.company = :company")
    Optional<Member> findByNameAndCompany(String name, Company company);

    @Query("select m from Member m join fetch m.company left join fetch m.passport where m.company.id = :companyId")
    List<Member> findAllByCompanyId(@Param("companyId") Long companyId);

    long countByCompanyId(Long companyId);

    void deleteAllByCompanyId(Long companyId);

    @Transactional
    @Modifying
    @Query("UPDATE Member m SET m.company = null WHERE m.company.id = :companyId")
    void nullifyMembersCompanyByCompanyId(Long companyId);

}
