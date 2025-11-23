package fun.kociarnia.bazy_danych_projekt.user;


import fun.kociarnia.bazy_danych_projekt.city.City;
import fun.kociarnia.bazy_danych_projekt.city.CityRepository;
import fun.kociarnia.bazy_danych_projekt.exception.*;
import fun.kociarnia.bazy_danych_projekt.restaurant.RestaurantRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final CityRepository cityRepository;
    private final RestaurantRepository restaurantRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository repository, CityRepository cityRepository, RestaurantRepository restaurantRepository) {
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
        this.cityRepository = cityRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public User getUserById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User", "id", id));
    }

    public User getUserByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User", "username", username));
    }

    public User createUser(User user) {
        validatePassword(user.getPassword());
        User newUser = new User();
        validateUsernameUnique(null, user.getUsername());
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        if (repository.count() == 0) {
            newUser.setRole(User.Role.ADMIN); // First user gets admin role
        }

        return repository.save(newUser);
    }

    public User updateUserSelf(Long id, User user) {
        User updatedUser = getUserById(id);

        validateUsernameUnique(id, user.getUsername());
        updatedUser.setUsername(user.getUsername());

        validateEmailUnique(id, user.getEmail());
        updatedUser.setEmail(user.getEmail());

        updatedUser.setUpdatedAt(java.time.LocalDateTime.now());
        return repository.save(updatedUser);
    }

    public User updateUserAdmin(Long id, User user, String cityName, Long restaurantId) {
        User updatedUser = getUserById(id);

        validateUsernameUnique(id, user.getUsername());
        updatedUser.setUsername(user.getUsername());

        validateEmailUnique(id, user.getEmail());
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
        return repository.save(updatedUser);
    }


    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    public User changePassword(Long id, String newPassword) {
        User updatedUser = getUserById(id);

        validatePassword(newPassword);

        String encodedPassword = passwordEncoder.encode(newPassword);


        if (updatedUser.getPassword().equals(encodedPassword)) {
            throw new SamePasswordException();
        }


        updatedUser.setPassword(encodedPassword);
        updatedUser.setUpdatedAt(java.time.LocalDateTime.now());
        return repository.save(updatedUser);
    }

    public List<User> getUsersFiltered(Long restaurantId, User.Role role, User.Status status, String cityName, String username, String email) {
        return repository.findAll(
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
        return repository.save(user);
    }

    public User changeStatus(Long userId, User.Status newStatus) {
        User user = getUserById(userId);
        if (user.getRole() == User.Role.ADMIN)
            throw new IllegalOperationException("Cannot block an admin");
        user.setStatus(newStatus);
        return repository.save(user);
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
        repository.findByUsername(username).ifPresent(existingUser -> {
            if (id == null || !Objects.equals(existingUser.getId(), id)) {
                throw new IllegalOperationException("Username already used.");
            }
        });
    }

    private void validateEmailUnique(Long id, String email) {
        repository.findByEmail(email).ifPresent(existingUser -> {
            if (!Objects.equals(existingUser.getId(), id)) {
                throw new IllegalOperationException("Email already used.");
            }
        });
    }

}