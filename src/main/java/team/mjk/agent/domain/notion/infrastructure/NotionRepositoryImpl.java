package team.mjk.agent.domain.notion.infrastructure;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.mjk.agent.domain.notion.domain.NotionRepository;
import team.mjk.agent.domain.notion.domain.Notion;
import team.mjk.agent.domain.notion.presentation.exception.NotionNotFoundException;

@RequiredArgsConstructor
@Repository
public class NotionRepositoryImpl implements NotionRepository {

  private final NotionJpaRepository notionJpaRepository;

  @Override
  public Notion save(Notion notion) {
    return notionJpaRepository.save(notion);
  }

  @Override
  public Notion findByCompanyId(Long companyId) {
    return notionJpaRepository.findByCompanyId(companyId)
        .orElseThrow(NotionNotFoundException::new);
  }

  @Override
  public Optional<Notion> findOptionalByCompanyId(Long companyId) {
    return notionJpaRepository.findByCompanyId(companyId);
  }

  @Override
  public void delete(Notion notion) {
    notionJpaRepository.delete(notion);
  }

  @Override
  public void deleteAllByCompanyId(Long companyId) {
    notionJpaRepository.deleteAllByCompanyId(companyId);
  }

}