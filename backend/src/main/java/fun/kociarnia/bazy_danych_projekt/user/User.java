package fun.kociarnia.bazy_danych_projekt.user;

import fun.kociarnia.bazy_danych_projekt.city.City;
import fun.kociarnia.bazy_danych_projekt.order.Order;
import fun.kociarnia.bazy_danych_projekt.restaurant.Restaurant;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    public enum Role {
        CLIENT, EMPLOYEE, ADMIN
    }

    public enum Status {
        ACTIVE, BLOCKED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.CLIENT;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = createdAt;

    @OneToMany(mappedBy = "client")
    private List<Order> orders;

    // For employees, link to the restaurant they work at
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}