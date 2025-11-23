package fun.kociarnia.bazy_danych_projekt.review;


import fun.kociarnia.bazy_danych_projekt.exception.IllegalOperationException;
import fun.kociarnia.bazy_danych_projekt.exception.NotFoundException;
import fun.kociarnia.bazy_danych_projekt.exception.ResourceAlreadyExistsException;
import fun.kociarnia.bazy_danych_projekt.order.Order;
import fun.kociarnia.bazy_danych_projekt.order.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ReviewService {

    private final ReviewRepository repository;
    private final OrderRepository orderRepository;

    public ReviewService(ReviewRepository repository, OrderRepository orderRepository) {
        this.repository = repository;
        this.orderRepository = orderRepository;
    }

    public List<Review> getAllReviews() {
        return repository.findAll();
    }

    public Review getReviewById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review", "id", id));
    }

    public Review createReview(Review review, Long orderId, Long currentUserId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order", "id", orderId));

        if (!Objects.equals(order.getClient().getId(), currentUserId))
            throw new IllegalOperationException("Cannot create a review for an order that does not belong to you.");

        if (repository.findByOrderId(orderId).isPresent()) {
            throw new ResourceAlreadyExistsException("Review for order with id " + orderId + " already exists");
        }
        Review newReview = new Review();
        newReview.setComment(review.getComment());
        newReview.setRating(review.getRating());
        newReview.setOrder(order);
        newReview.setAnonymous(review.isAnonymous());
        return repository.save(newReview);
    }

    public void deleteReview(Long id) {
        repository.deleteById(id);
    }

    public Review getReviewByOrder(Long orderId) {
        return repository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException("Review", "orderId", orderId));
    }

    public List<Review> getReviewsByOfferId(Long offerId) {
        return repository.findByOrder_Offer_Id(offerId);
    }

    public List<Review> getReviewsByClientId(Long userId) {
        return repository.findByOrder_Client_Id(userId);
    }
}
