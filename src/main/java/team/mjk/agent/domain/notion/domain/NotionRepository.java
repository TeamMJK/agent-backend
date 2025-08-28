package team.mjk.agent.domain.notion.domain;


public interface NotionRepository {
  Notion save(Notion notion);
  Notion findByCompanyId(Long companyId);

  void delete(Notion notion);

}
