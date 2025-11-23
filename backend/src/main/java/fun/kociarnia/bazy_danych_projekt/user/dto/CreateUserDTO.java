package fun.kociarnia.bazy_danych_projekt.user.dto;

import fun.kociarnia.bazy_danych_projekt.restaurant.Restaurant;
import fun.kociarnia.bazy_danych_projekt.user.User;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateUserDTO {
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters")
    private String username;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    public static User toEntity(CreateUserDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setStatus(User.Status.ACTIVE);
        return user;
    }
}