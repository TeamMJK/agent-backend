package team.mjk.agent.domain.hotel.dto;

public record VncBusinessInfo(
    String hotel_destination,
    String booking_dates,
    int guests,
    int budget
) {
}