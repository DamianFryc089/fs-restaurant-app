package fun.kociarnia.bazy_danych_projekt.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    List<User> findByRestaurantId(Long restaurantId);

    List<User> findByRole(User.Role role);

    List<User> findByRestaurant_Id(Long restaurantId);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}