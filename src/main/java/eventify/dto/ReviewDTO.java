package eventify.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewDTO {
    private Long id;

    @NotNull(message = "Event ID is required")
    private Long eventId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Review text cannot be blank")
    @Size(max = 500, message = "Review text must be at most 500 characters")
    private String reviewText;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer reviewRating;
}
