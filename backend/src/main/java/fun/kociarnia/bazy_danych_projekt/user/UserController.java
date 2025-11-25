package fun.kociarnia.bazy_danych_projekt.user;

import fun.kociarnia.bazy_danych_projekt.MyUserDetails;
import fun.kociarnia.bazy_danych_projekt.city.dto.CityDTO;
import fun.kociarnia.bazy_danych_projekt.user.dto.CreateUserDTO;
import fun.kociarnia.bazy_danych_projekt.user.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getUsers(
            @RequestParam(required = false) Long restaurantId,
            @RequestParam(required = false) User.Role role,
            @RequestParam(required = false) User.Status status,
            @RequestParam(required = false) String cityName,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email
    ) {
        List<User> users = service.getUsersFiltered(restaurantId, role, status, cityName, username, email);
        return UserDTO.fromEntityList(users);
    }

    @PostMapping
    public UserDTO createUser(@Valid @RequestBody CreateUserDTO dto) {
        User user = service.createUser(CreateUserDTO.toEntity(dto));
        return UserDTO.fromEntity(user);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("isAuthenticated() and #userId == authentication.principal.id")
    public UserDTO updateUserSelf(@PathVariable Long userId, @Valid @RequestBody UserDTO dto) {
        User user = UserDTO.toEntity(dto);
        return UserDTO.fromEntity(service.updateUserSelf(userId, user));
    }

    @PutMapping("/admin/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO updateUserAdmin(@PathVariable Long userId, @Valid @RequestBody UserDTO dto) {
        User user = UserDTO.toEntity(dto);
        return UserDTO.fromEntity(service.updateUserAdmin(userId, user,dto.getCityName(), dto.getRestaurantId()));
    }

    @PutMapping("/changePassword/{userId}")
    @PreAuthorize("isAuthenticated() and #userId == authentication.principal.id")
    public UserDTO changePassword(@PathVariable Long userId, @Valid @RequestBody CreateUserDTO dto) {
        User user = CreateUserDTO.toEntity(dto);
        return UserDTO.fromEntity(service.changePassword(userId, user.getPassword()));
    }

    @PutMapping("/changeStatus/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO changeStatus(@PathVariable Long userId, @Valid @RequestBody UserDTO dto) {
        User user = UserDTO.toEntity(dto);
        return UserDTO.fromEntity(service.changeStatus(userId, user.getStatus()));
    }

    @PutMapping("/setCity/{userId}")
    @PreAuthorize("isAuthenticated() and #userId == authentication.principal.id")
    public UserDTO changeUserCity(@PathVariable Long userId, @RequestBody CityDTO dto) {
        return UserDTO.fromEntity(service.changeUserCity(userId, dto.getId()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
    }
}