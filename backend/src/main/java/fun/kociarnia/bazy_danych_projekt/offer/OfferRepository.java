package fun.kociarnia.bazy_danych_projekt.offer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    Optional<List<Offer>> findByRestaurantId(Long restaurantId);
    List<Offer> findAllByStatus(Offer.Status status);
    List<Offer> findByRestaurant_City_NameIgnoreCase(String cityName);

    List<Offer> findByRestaurant_City_Id(Long restaurant_city_id);
}