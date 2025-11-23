package fun.kociarnia.bazy_danych_projekt.order.dto;

import fun.kociarnia.bazy_danych_projekt.city.City;
import fun.kociarnia.bazy_danych_projekt.city.dto.CityDTO;
import fun.kociarnia.bazy_danych_projekt.offer.Offer;
import fun.kociarnia.bazy_danych_projekt.order.Order;
import fun.kociarnia.bazy_danych_projekt.review.Review;
import fun.kociarnia.bazy_danych_projekt.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
public class OrderDTO {
    private Long id;

    @NotNull
    private Long clientId;

    private String status = "PENDING";

    private BigDecimal totalPrice;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @NotNull
    private Long offerId;

    private String offerTitle;

    private String restaurantId;

    private String restaurantName;

    @NotNull
    @Min(1)
    private Integer quantity;

    private Long reviewId;

    public static OrderDTO fromEntity(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setClientId(order.getClient().getId());
        dto.setStatus(order.getStatus().name());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        dto.setOfferId(order.getOffer().getId());
        dto.setOfferTitle(order.getOffer().getTitle());
        dto.setRestaurantId(order.getOffer().getRestaurant().getId().toString());
        dto.setRestaurantName(order.getOffer().getRestaurant().getName());
        dto.setQuantity(order.getQuantity());
        if (order.getReview() != null) {
            dto.setReviewId(order.getReview().getId());
        }
        return dto;
    }

    public static List<OrderDTO> fromEntityList(List<Order> orders) {
        return orders.stream()
                .map(OrderDTO::fromEntity)
                .toList();
    }

    public static Order toEntity(OrderDTO dto) {
        Order order = new Order();
        order.setId(dto.getId());
        Optional<Order.Status> status = Optional.of(Order.Status.valueOf(dto.getStatus()));
        order.setStatus(status.orElse(Order.Status.PENDING));
        order.setTotalPrice(dto.getTotalPrice());
        order.setQuantity(dto.getQuantity());
        return order;
    }
}

