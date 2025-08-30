package team.mjk.agent.domain.company.domain;

import static jakarta.persistence.EnumType.STRING;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Company {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String name;

  @Convert(converter = WorkspaceListConverter.class)
  private List<Workspace> workspace;

  @Builder
  private Company(String name, List<Workspace> workspace) {
    this.name = name;
    this.workspace = workspace;
  }

  public static Company create(String name, List<Workspace> workspace) {
    return Company.builder()
        .name(name)
        .workspace(workspace)
        .build();
  }

  public void update(String name, List<Workspace> workspace) {
    this.name = name;
    this.workspace = workspace;
  }

  public void updateName(String name) {
    this.name = name;
  }

  public void updateWorkspace(List<Workspace> workspace) {
    this.workspace = workspace;
  }

}