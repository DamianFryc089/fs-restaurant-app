package fun.kociarnia.bazy_danych_projekt.restaurant.dto;


import fun.kociarnia.bazy_danych_projekt.restaurant.Restaurant;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class RestaurantDTO {
    private Long id;
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    @Size(min = 2, max = 100, message = "City name must be between 2 and 100 characters")
    private String cityName;
    @Size(min = 2, max = 200, message = "Street must be between 2 and 200 characters")
    private String street;
    @Min(0)
    @Max(9999999999L)
    private String phone;

    public static RestaurantDTO fromEntity(Restaurant restaurant) {
        RestaurantDTO dto = new RestaurantDTO();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setCityName(restaurant.getCity().getName());
        dto.setStreet(restaurant.getStreet());
        dto.setPhone(restaurant.getPhone());
        return dto;
    }

    public static List<RestaurantDTO> fromEntityList(List<Restaurant> offers) {
        return offers.stream()
                .map(RestaurantDTO::fromEntity)
                .toList();
    }

    public static Restaurant toEntity(RestaurantDTO dto) {
        Restaurant offer = new Restaurant();
        offer.setId(dto.getId());
        offer.setName(dto.getName());
        offer.setStreet(dto.getStreet());
        offer.setPhone(dto.getPhone());
        return offer;
    }

}
