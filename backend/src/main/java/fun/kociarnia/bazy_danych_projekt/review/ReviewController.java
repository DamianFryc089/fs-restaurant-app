package fun.kociarnia.bazy_danych_projekt.review;


import fun.kociarnia.bazy_danych_projekt.MyUserDetails;
import fun.kociarnia.bazy_danych_projekt.review.dto.ReviewDTO;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bazy/reviews")
public class ReviewController {

    private final ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }

    @GetMapping
    public List<ReviewDTO> getAllReviews() {
        List<Review> reviews = service.getAllReviews();
        return ReviewDTO.fromEntityList(reviews);
    }

    @GetMapping("/{id}")
    public ReviewDTO getReviewById(@PathVariable Long id) {
        Review review = service.getReviewById(id);
        return ReviewDTO.fromEntity(review);
    }

    @GetMapping("/offer/{offerId}")
    public List<ReviewDTO> getReviewsByOfferId(@PathVariable Long offerId) {
        List<Review> reviews = service.getReviewsByOfferId(offerId);
        return ReviewDTO.fromEntityList(reviews);
    }

    @GetMapping("/user/{userId}")
    public List<ReviewDTO> getReviewsByClientId(@PathVariable Long userId) {
        List<Review> reviews = service.getReviewsByClientId(userId);
        return ReviewDTO.fromEntityList(reviews);
    }

    // check if the user is owner of the review
    @PostMapping
    @PreAuthorize("isAuthenticated() and hasRole('CLIENT')")
    public ReviewDTO createReview(@Valid @RequestBody ReviewDTO dto, @AuthenticationPrincipal MyUserDetails currentUser) {
        Review review = ReviewDTO.toEntity(dto);
        Review createdReview = service.createReview(review, dto.getOrderId(), currentUser.getId());
        return ReviewDTO.fromEntity(createdReview);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteReview(@PathVariable Long id) {
        service.deleteReview(id);
    }

    @GetMapping("/order/{orderId}")
    public ReviewDTO getReviewByOrder(@PathVariable Long orderId) {
        Review review = service.getReviewByOrder(orderId);
        return ReviewDTO.fromEntity(review);
    }
}