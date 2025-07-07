package team.mjk.agent.domain.businessTrip.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record BusinessTripUpdateRequest(
    @NotNull(message = "출발일을 선택해주세요.")
    LocalDate departDate,

    @NotNull(message = "도착일을 선택해주세요.")
    LocalDate arriveDate,

    @NotBlank(message = "목적지를 입력해주세요.")
    String destination,

    @NotBlank(message = "출장 가는 인원들의 성명을 입력해주세요.")
    List<String> names
) {

}
