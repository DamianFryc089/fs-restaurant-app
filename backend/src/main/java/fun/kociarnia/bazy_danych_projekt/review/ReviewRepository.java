package fun.kociarnia.bazy_danych_projekt.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByOrderId(Long orderId);

    List<Review> findByOrder_Offer_Id(Long offerId);

    List<Review> findByOrder_Client_Id(Long userId);
}