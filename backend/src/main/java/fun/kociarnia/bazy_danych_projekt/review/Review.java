package fun.kociarnia.bazy_danych_projekt.review;

import fun.kociarnia.bazy_danych_projekt.order.Order;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reviews")
@Check(constraints = "rating >= 1 AND rating <= 5")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Integer rating;

    @Column(length = 1000)
    private String comment;

    private boolean isAnonymous = false;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
