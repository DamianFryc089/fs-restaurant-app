package fun.kociarnia.bazy_danych_projekt.user.dto;

import fun.kociarnia.bazy_danych_projekt.city.City;
import fun.kociarnia.bazy_danych_projekt.restaurant.Restaurant;
import fun.kociarnia.bazy_danych_projekt.restaurant.dto.RestaurantDTO;
import fun.kociarnia.bazy_danych_projekt.user.User;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private Long id;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters")
    private String username;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;
    private String role;
    private String status;
    private String cityName;
    private Long restaurantId;

    public static UserDTO fromEntity(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        dto.setStatus(user.getStatus().name());

        if (user.getCity() != null)
            dto.setCityName(user.getCity().getName());

        if (user.getRestaurant() != null)
            dto.setRestaurantId(user.getRestaurant().getId());

        return dto;
    }
    public static List<UserDTO> fromEntityList(List<User> offers) {
        return offers.stream()
                .map(UserDTO::fromEntity)
                .toList();
    }
    public static User toEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        if (dto.getStatus() != null) {
            user.setStatus(User.Status.valueOf(dto.getStatus()));
        }
        if (dto.getRole() != null) {
            user.setRole(User.Role.valueOf(dto.getRole()));
        }
        return user;
    }
}