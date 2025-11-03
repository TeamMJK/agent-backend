package team.mjk.agent.domain.hotel.dto;

import java.util.List;

public record VncBusinessInfo(
    String hotel_destination,
    String booking_dates,
    int guests,
    int budget,
    List<String> requirements
) {
}