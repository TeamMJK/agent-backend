package team.mjk.agent.domain.businessTrip.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;

@Builder
public record BusinessTripAgentRequest(
    @JsonProperty("departDate")
    String departDate,
    @JsonProperty("arriveDate")
    String arriveDate,
    @JsonProperty("destination")
    String destination,
    @JsonProperty("names")
    List<String> names,
    @JsonProperty("serviceType")
    String serviceType
) {

}