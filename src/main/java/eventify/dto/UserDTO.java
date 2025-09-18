package eventify.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long id;

    @NotBlank(message = "First name cannot be blank")
    @Size(max = 50, message = "First name must be at most 50 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 50, message = "Last name must be at most 50 characters")
    private String lastName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Past(message = "Birth date must be in the past")
    private LocalDateTime birthDate;
}
