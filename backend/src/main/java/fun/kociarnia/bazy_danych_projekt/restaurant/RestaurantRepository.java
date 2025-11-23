package fun.kociarnia.bazy_danych_projekt.restaurant;

import fun.kociarnia.bazy_danych_projekt.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Object> findByName(String name);

    List<Restaurant> findByCity_Id(Long cityId);
}