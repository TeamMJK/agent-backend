package team.mjk.agent.domain.company.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import java.io.IOException;
import java.util.List;

public class WorkspaceListConverter implements AttributeConverter<List<Workspace>,String> {

  private static final ObjectMapper mapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,false)
      .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,false);

  @Override
  public String convertToDatabaseColumn(List<Workspace> workspaces) {
    try {
      return mapper.writeValueAsString(workspaces);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public List<Workspace> convertToEntityAttribute(String s) {
    try {
      return mapper.readValue(s, new TypeReference<>() {
      });
    } catch (IOException e) {
      throw new IllegalArgumentException();
    }
  }

}