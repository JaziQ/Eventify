package eventify.restControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import eventify.dto.ReviewDTO;
import eventify.mapper.Mapper;
import eventify.model.Review;
import eventify.model.User;
import eventify.model.Event;
import eventify.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReviewController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewService reviewService;

    private Review review;
    private ReviewDTO reviewDTO;

    @BeforeEach
    void setUp() {
        review = new Review();
        review.setId(1L);
        review.setUser(new User());
        review.setEvent(new Event());
        review.setReviewText("Great event!");
        review.setReviewRating(5);

        reviewDTO = Mapper.toReviewDTO(review);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAllReviews() throws Exception {
        Mockito.when(reviewService.getAllReviews()).thenReturn(List.of(review));

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(reviewDTO.getId()))
                .andExpect(jsonPath("$[0].reviewText").value(reviewDTO.getReviewText()))
                .andExpect(jsonPath("$[0].reviewRating").value(reviewDTO.getReviewRating()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getReviewById() throws Exception {
        Mockito.when(reviewService.getReviewById(1L)).thenReturn(review);

        mockMvc.perform(get("/api/reviews/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reviewDTO.getId()))
                .andExpect(jsonPath("$.reviewText").value(reviewDTO.getReviewText()))
                .andExpect(jsonPath("$.reviewRating").value(reviewDTO.getReviewRating()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getReviewByIdNotFound() throws Exception {
        Mockito.when(reviewService.getReviewById(anyLong()))
                .thenThrow(new EntityNotFoundException("Review not found"));

        mockMvc.perform(get("/api/reviews/99"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Review not found"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getReviewsByEventId() throws Exception {
        Mockito.when(reviewService.getReviewsByEventId(1L)).thenReturn(List.of(review));

        mockMvc.perform(get("/api/reviews/event/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(reviewDTO.getId()))
                .andExpect(jsonPath("$[0].reviewText").value(reviewDTO.getReviewText()))
                .andExpect(jsonPath("$[0].reviewRating").value(reviewDTO.getReviewRating()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void createReview() throws Exception {
        Mockito.when(reviewService.save(any(Review.class))).thenReturn(review);

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(reviewDTO.getId()))
                .andExpect(jsonPath("$.reviewText").value(reviewDTO.getReviewText()))
                .andExpect(jsonPath("$.reviewRating").value(reviewDTO.getReviewRating()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void updateReview() throws Exception {
        User user = new User();
        user.setId(1L);

        Event event = new Event();
        event.setId(1L);

        Review updatedReview = new Review();
        updatedReview.setId(1L);
        updatedReview.setUser(user);
        updatedReview.setEvent(event);
        updatedReview.setReviewText("Updated review text");
        updatedReview.setReviewRating(4);

        ReviewDTO updatedDTO = Mapper.toReviewDTO(updatedReview);

        Mockito.when(reviewService.updateReview(anyLong(), any(Review.class))).thenReturn(updatedReview);

        mockMvc.perform(put("/api/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedDTO.getId()))
                .andExpect(jsonPath("$.reviewText").value(updatedDTO.getReviewText()))
                .andExpect(jsonPath("$.reviewRating").value(updatedDTO.getReviewRating()))
                .andExpect(jsonPath("$.userId").value(user.getId()))
                .andExpect(jsonPath("$.eventId").value(event.getId()));
    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void deleteReview() throws Exception {
        Mockito.doNothing().when(reviewService).deleteReview(1L);

        mockMvc.perform(delete("/api/reviews/1"))
                .andExpect(status().isNoContent());
    }
}
