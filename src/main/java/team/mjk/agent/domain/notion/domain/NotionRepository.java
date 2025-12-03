package team.mjk.agent.domain.notion.domain;


import java.util.Optional;

public interface NotionRepository {

    Notion save(Notion notion);

    Notion findByCompanyId(Long companyId);

    Optional<Notion> findOptionalByCompanyId(Long companyId);

    void delete(Notion notion);

    void deleteAllByCompanyId(Long companyId);

}