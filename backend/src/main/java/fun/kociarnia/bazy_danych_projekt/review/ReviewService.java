package fun.kociarnia.bazy_danych_projekt.review;


import fun.kociarnia.bazy_danych_projekt.exception.IllegalOperationException;
import fun.kociarnia.bazy_danych_projekt.exception.NotFoundException;
import fun.kociarnia.bazy_danych_projekt.exception.ResourceAlreadyExistsException;
import fun.kociarnia.bazy_danych_projekt.order.Order;
import fun.kociarnia.bazy_danych_projekt.order.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review", "id", id));
    }

    public Review createReview(Review review, Long orderId, Long currentUserId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order", "id", orderId));

        if (!Objects.equals(order.getClient().getId(), currentUserId))
            throw new IllegalOperationException("Cannot create a review for an order that does not belong to you.");

        if (reviewRepository.findByOrderId(orderId).isPresent())
            throw new ResourceAlreadyExistsException("Review for order with id " + orderId + " already exists");

        if (review.getRating() > 5 || review.getRating() < 1)
            throw new IllegalOperationException("Rating for a review must be from 1 to 5");

        Review newReview = new Review();
        newReview.setComment(review.getComment());
        newReview.setRating(review.getRating());
        newReview.setOrder(order);
        newReview.setAnonymous(review.isAnonymous());
        Review savedReview = reviewRepository.save(newReview);
        logger.info("Review created: reviewId={}, orderId={}, clientId={}, rating={}, anonymous={}",
                savedReview.getId(), orderId, currentUserId, savedReview.getRating(), savedReview.isAnonymous());
        return savedReview;
    }

    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review", "id", id));

        Order order = review.getOrder();
        order.setReview(null);
        orderRepository.save(order);

        reviewRepository.delete(review);
        logger.info("Review deleted: reviewId={}, orderId={}, clientId={}",
                review.getId(), review.getOrder().getId(), review.getOrder().getClient().getId());
        reviewRepository.flush();
    }

    public Review getReviewByOrder(Long orderId) {
        return reviewRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException("Review", "orderId", orderId));
    }

    public List<Review> getReviewsByOfferId(Long offerId) {
        return reviewRepository.findByOrder_Offer_Id(offerId);
    }

    public List<Review> getReviewsByClientId(Long userId) {
        return reviewRepository.findByOrder_Client_Id(userId);
    }
}
