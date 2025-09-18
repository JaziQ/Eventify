package eventify.dto;

import eventify.model.Booking;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookingDTO {
    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Event ID is required")
    private Long eventId;

    @NotNull(message = "Booking status is required")
    private Booking.BookingStatus bookingStatus;

    private LocalDateTime booking_date;
}
