package fun.kociarnia.bazy_danych_projekt.user;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> filter(Long restaurantId, User.Role role, User.Status status, String cityName, String username, String email) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (restaurantId != null)
                predicates.add(cb.equal(root.get("restaurant").get("id"), restaurantId));

            if (role != null)
                predicates.add(cb.equal(root.get("role"), role));

            if (status != null)
                predicates.add(cb.equal(root.get("status"), status));

            if (cityName != null)
                predicates.add(cb.equal(root.get("city").get("name"), cityName));

            if (username != null)
                predicates.add(cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%"));

            if (email != null)
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

