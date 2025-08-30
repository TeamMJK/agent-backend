package team.mjk.agent.domain.mcp.notion.domain;


public interface NotionRepository {
  Notion save(Notion notion);
  Notion findByCompanyId(Long companyId);

  void delete(Notion notion);

}
