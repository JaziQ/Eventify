package eventify.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "event_id"})})
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    private String reviewText;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime reviewDate;

    @Min(1)
    @Max(5)
    private Integer reviewRating;

}
