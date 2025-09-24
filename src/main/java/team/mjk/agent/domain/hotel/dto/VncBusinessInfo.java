package team.mjk.agent.domain.hotel.dto;

public record Detail(
    String hotel_destination,
    String booking_dates,
    int guests,
    int budget
) {
}