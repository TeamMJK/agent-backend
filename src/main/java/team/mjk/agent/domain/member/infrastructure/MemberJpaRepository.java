package team.mjk.agent.domain.member.infrastructure;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.member.application.dto.response.MemberInfoGetResponse;
import team.mjk.agent.domain.member.domain.Member;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNameAndCompany(String name, Company company);

    List<Member> findAllByCompanyId(@Param("companyId") Long companyId);

    @Query("select new team.mjk.agent.domain.member.application.dto.response.MemberInfoGetResponse(" +
            "m.name, m.email, m.firstName, m.lastName, m.phoneNumber, m.gender, m.birthDate, " +
            "p.passportNumber, p.passportExpireDate) " +
            "from Member m " +
            "join m.company c " +
            "left join m.passport p " +
            "where c.id = :companyId")
    List<MemberInfoGetResponse> findAllMemberInfoByCompanyId(@Param("companyId") Long companyId);

    long countByCompanyId(Long companyId);

    void deleteAllByCompanyId(Long companyId);

    @Transactional
    @Modifying
    @Query("UPDATE Member m SET m.company = null WHERE m.company.id = :companyId")
    void nullifyMembersCompanyByCompanyId(Long companyId);

}
