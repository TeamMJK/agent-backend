package team.mjk.agent.domain.businessTrip.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import team.mjk.agent.domain.businessTrip.presentation.exception.ConversionErrorException;

import java.util.List;

@Converter
public class NameListConverter implements AttributeConverter<List<String>,String> {

  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(List<String> names) {
    try {
      return mapper.writeValueAsString(names);
    } catch (JsonProcessingException e) {
      throw new ConversionErrorException();
    }
  }

  @Override
  public List<String> convertToEntityAttribute(String data) {
    try {
      return mapper.readValue(data, List.class);
    } catch (JsonProcessingException e) {
      throw new ConversionErrorException();
    }
  }
}
