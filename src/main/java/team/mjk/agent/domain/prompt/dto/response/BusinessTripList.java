package team.mjk.agent.domain.prompt.dto.response;

import java.util.List;

public record BusinessTripList(
    List<BusinessTripInfo> businessTripInfoList
) {

}