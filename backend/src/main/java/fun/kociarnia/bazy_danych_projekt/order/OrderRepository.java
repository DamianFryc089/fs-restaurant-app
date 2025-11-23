package fun.kociarnia.bazy_danych_projekt.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByClientIdOrderByCreatedAtDesc(Long clientId);
    List<Order> findByOffer_Restaurant_Id(Long restaurantId);
}