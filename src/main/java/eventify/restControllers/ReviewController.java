package eventify.restControllers;

import eventify.dto.ReviewDTO;
import eventify.mapper.Mapper;
import eventify.model.Review;
import eventify.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public List<ReviewDTO> getAllReviews() {
        return reviewService.getAllReviews().stream()
                .map(Mapper::toReviewDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ReviewDTO getReviewById(@PathVariable Long id) {
        try {
            return Mapper.toReviewDTO(reviewService.getReviewById(id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found");
        }
    }

    @GetMapping("/event/{eventId}")
    public List<ReviewDTO> getReviewsByEventId(@PathVariable Long eventId) {
        return reviewService.getReviewsByEventId(eventId).stream()
                .map(Mapper::toReviewDTO)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDTO createReview(@Valid @RequestBody Review review) {
        return Mapper.toReviewDTO(reviewService.save(review));
    }

    @PutMapping("/{id}")
    public ReviewDTO updateReview(@PathVariable Long id, @Valid @RequestBody Review updatedReview) {
        try {
            return Mapper.toReviewDTO(reviewService.updateReview(id, updatedReview));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found");
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable Long id) {
        try {
            reviewService.deleteReview(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found");
        }
    }
}
