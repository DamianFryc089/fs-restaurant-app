package fun.kociarnia.bazy_danych_projekt.offer.dto;


import fun.kociarnia.bazy_danych_projekt.city.City;
import fun.kociarnia.bazy_danych_projekt.offer.Offer;
import fun.kociarnia.bazy_danych_projekt.order.Order;
import fun.kociarnia.bazy_danych_projekt.review.Review;
import fun.kociarnia.bazy_danych_projekt.restaurant.Restaurant;
import fun.kociarnia.bazy_danych_projekt.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
public class OfferDTO {
    private Long id;

    private Long restaurantId;

    private String restaurantName;

    private String title;

    private String description;

    private BigDecimal price;

    private Integer availableQuantity;

    private String status;

    private Double rating = 0.0;

    public static OfferDTO fromEntity(Offer offer) {
        OfferDTO dto = new OfferDTO();
        dto.setId(offer.getId());
        dto.setRestaurantId(offer.getRestaurant().getId());
        dto.setRestaurantName(offer.getRestaurant().getName());
        dto.setTitle(offer.getTitle());
        dto.setDescription(offer.getDescription());
        dto.setPrice(offer.getPrice());
        dto.setAvailableQuantity(offer.getAvailableQuantity());
        dto.setStatus(offer.getStatus().name());

        double average = offer.getOrders().stream()
                .map(Order::getReview)
                .filter(Objects::nonNull)
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);

        dto.setRating(average);
        return dto;
    }

    public static List<OfferDTO> fromEntityList(List<Offer> offers) {
        return offers.stream()
                .map(OfferDTO::fromEntity)
                .toList();
    }

    public static Offer toEntity(OfferDTO dto) {
        Offer offer = new Offer();
        offer.setId(dto.getId());
        offer.setTitle(dto.getTitle());
        offer.setDescription(dto.getDescription());
        offer.setPrice(dto.getPrice());
        offer.setAvailableQuantity(dto.getAvailableQuantity());

        if (dto.getStatus() != null) {
            offer.setStatus(Offer.Status.valueOf(dto.getStatus()));
        }
        return offer;
    }
}
