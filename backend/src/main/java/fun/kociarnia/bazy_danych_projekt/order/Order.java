package fun.kociarnia.bazy_danych_projekt.order;

import fun.kociarnia.bazy_danych_projekt.offer.Offer;
import fun.kociarnia.bazy_danych_projekt.review.Review;
import fun.kociarnia.bazy_danych_projekt.user.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
@Check(constraints = "quantity >= 1 AND total_price >= 0")
public class Order {

    public enum Status {
        PENDING, CANCELLED, COMPLETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private BigDecimal totalPrice;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = createdAt;

    @ManyToOne
    @JoinColumn(name = "offer_id", nullable = false)
    private Offer offer;

    @Column(nullable = false)
    private Integer quantity;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Review review;
}
