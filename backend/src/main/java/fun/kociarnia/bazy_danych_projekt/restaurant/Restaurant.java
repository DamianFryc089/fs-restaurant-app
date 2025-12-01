package fun.kociarnia.bazy_danych_projekt.restaurant;

import fun.kociarnia.bazy_danych_projekt.city.City;
import fun.kociarnia.bazy_danych_projekt.offer.Offer;
import fun.kociarnia.bazy_danych_projekt.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;


    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;
    private String street;

    private String phone;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "restaurant")
    private List<User> employees;

    @OneToMany(mappedBy = "restaurant")
    private List<Offer> offers;

}

