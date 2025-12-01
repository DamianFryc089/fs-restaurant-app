package fun.kociarnia.bazy_danych_projekt.user;


import fun.kociarnia.bazy_danych_projekt.city.City;
import fun.kociarnia.bazy_danych_projekt.city.CityRepository;
import fun.kociarnia.bazy_danych_projekt.exception.IllegalOperationException;
import fun.kociarnia.bazy_danych_projekt.exception.NotFoundException;
import fun.kociarnia.bazy_danych_projekt.exception.SamePasswordException;
import fun.kociarnia.bazy_danych_projekt.exception.WeakPasswordException;
import fun.kociarnia.bazy_danych_projekt.restaurant.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final RestaurantRepository restaurantRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User", "id", id));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User", "username", username));
    }

    public User createUser(User user) {
        validatePassword(user.getPassword());
        validateUsernameUnique(null, user.getUsername());

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        // First user gets admin role
        if (userRepository.count() == 0) newUser.setRole(User.Role.ADMIN);

        User savedUser = userRepository.save(newUser);
        logger.info("New user created: username={}, email={}, role={}",
                savedUser.getUsername(), savedUser.getEmail(), savedUser.getRole());
        return savedUser;
    }

    public User updateUserSelf(Long id, User user) {
        validateUsernameUnique(id, user.getUsername());
        validateEmailUnique(id, user.getEmail());

        User updatedUser = getUserById(id);
        updatedUser.setUsername(user.getUsername());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setUpdatedAt(java.time.LocalDateTime.now());

        User savedUser = userRepository.save(updatedUser);
        logger.info("User updated self: id={}, username={}, email={}",
                savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
        return savedUser;
    }

    public User updateUserAdmin(Long id, User user, String cityName, Long restaurantId) {
        validateUsernameUnique(id, user.getUsername());
        validateEmailUnique(id, user.getEmail());

        User updatedUser = getUserById(id);
        updatedUser.setUsername(user.getUsername());
        updatedUser.setEmail(user.getEmail());

        if (updatedUser.getRole() == User.Role.ADMIN && user.getRole() != User.Role.ADMIN)
            throw new IllegalOperationException("Cannot change role of an admin user.");
        updatedUser.setRole(user.getRole());

        if (Objects.equals(cityName, "")) updatedUser.setCity(null);
        else cityRepository.findByName(cityName).ifPresentOrElse(updatedUser::setCity, () -> {
            throw new NotFoundException("City", "city_name", cityName);
        });

        if (restaurantId == 0) updatedUser.setRestaurant(null);
        else restaurantRepository.findById(restaurantId).ifPresentOrElse(updatedUser::setRestaurant, () -> {
            throw new NotFoundException("Restaurant", "id", restaurantId);
        });

        updatedUser.setUpdatedAt(java.time.LocalDateTime.now());

        User savedUser = userRepository.save(updatedUser);
        logger.info("Admin updated user: id={}, username={}, email={}, role={}, city={}, restaurant={}",
                savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getCity() != null ? savedUser.getCity().getName() : null,
                savedUser.getRestaurant() != null ? savedUser.getRestaurant().getName() : null);
        return savedUser;
    }


    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.deleteById(id);
        logger.info("User deleted: id={}, username={}, email={}", id, user.getUsername(), user.getEmail());
    }

    public User changePassword(Long id, String newPassword) {
        validatePassword(newPassword);

        User updatedUser = getUserById(id);
        String encodedPassword = passwordEncoder.encode(newPassword);

        if (updatedUser.getPassword().equals(encodedPassword)) {
            throw new SamePasswordException();
        }

        updatedUser.setPassword(encodedPassword);
        updatedUser.setUpdatedAt(java.time.LocalDateTime.now());

        User savedUser = userRepository.save(updatedUser);
        logger.info("User changed password: id={}, username={}", savedUser.getId(), savedUser.getUsername());
        return savedUser;
    }

    public List<User> getUsersFiltered(Long restaurantId, User.Role role, User.Status status, String cityName, String username, String email) {
        return userRepository.findAll(
                UserSpecification.filter(
                        restaurantId, role, status, cityName, username, email
                )
        );
    }

    public User changeUserCity(Long userId, Long cityId) {
        User user = getUserById(userId);
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new NotFoundException("City", "id", cityId));
        user.setCity(city);
        User savedUser = userRepository.save(user);
        logger.info("User city changed: id={}, username={}, newCity={}", savedUser.getId(), savedUser.getUsername(), city.getName());
        return savedUser;
    }

    public User changeStatus(Long userId, User.Status newStatus) {
        User user = getUserById(userId);
        if (user.getRole() == User.Role.ADMIN)
            throw new IllegalOperationException("Cannot block an admin");
        user.setStatus(newStatus);
        User savedUser = userRepository.save(user);
        logger.info("User status changed: id={}, username={}, newStatus={}", savedUser.getId(), savedUser.getUsername(), newStatus);
        return savedUser;
    }

    private void validatePassword(String password) {
        List<String> errors = new ArrayList<>();

        if (password == null || password.isEmpty()) {
            errors.add("Password cannot be null or empty");
        } else {
            if (password.length() < 8) errors.add("At least 8 characters");
            if (!password.matches(".*[A-Z].*")) errors.add("At least one uppercase letter");
            if (!password.matches(".*[a-z].*")) errors.add("At least one lowercase letter");
            if (!password.matches(".*\\d.*")) errors.add("At least one digit");
            if (!password.matches(".*[!@#$%^&*()].*")) errors.add("At least one special character (!@#$%^&*)");
        }

        if (!errors.isEmpty()) {
            throw new WeakPasswordException(errors);
        }
    }

    private void validateUsernameUnique(Long id, String username) {
        userRepository.findByUsername(username).ifPresent(existingUser -> {
            if (id == null || !Objects.equals(existingUser.getId(), id)) {
                throw new IllegalOperationException("Username already used.");
            }
        });
    }

    private void validateEmailUnique(Long id, String email) {
        userRepository.findByEmail(email).ifPresent(existingUser -> {
            if (!Objects.equals(existingUser.getId(), id)) {
                throw new IllegalOperationException("Email already used.");
            }
        });
    }

}