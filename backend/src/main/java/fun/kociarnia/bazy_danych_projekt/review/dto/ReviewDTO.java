package fun.kociarnia.bazy_danych_projekt.review.dto;

import fun.kociarnia.bazy_danych_projekt.review.Review;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class ReviewDTO {
    private Long id;

    @NotNull
    private Long orderId;

    private String username;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    private boolean anonymous;

    @NotNull
    @Size(max = 1000)
    private String comment;

    private LocalDateTime createdAt;

    public static ReviewDTO fromEntity(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setOrderId(review.getOrder().getId());
        dto.setUsername(review.isAnonymous() ? "anonymoys" : review.getOrder().getClient().getUsername());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }

    public static List<ReviewDTO> fromEntityList(List<Review> offers) {
        return offers.stream()
                .map(ReviewDTO::fromEntity)
                .toList();
    }

    public static Review toEntity(ReviewDTO dto) {
        Review review = new Review();
        review.setId(dto.getId());
        review.setAnonymous(dto.isAnonymous());
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        return review;
    }
}
