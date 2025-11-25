package fun.kociarnia.bazy_danych_projekt.restaurant;

import fun.kociarnia.bazy_danych_projekt.restaurant.dto.RestaurantDTO;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService service;

    public RestaurantController(RestaurantService service) {
        this.service = service;
    }

    @GetMapping
    public List<RestaurantDTO> getAllRestaurants() {
        List<Restaurant> restaurants = service.getAllRestaurants();
        return RestaurantDTO.fromEntityList(restaurants);
    }

    @GetMapping("/{id}")
    public RestaurantDTO getRestaurantById(@PathVariable Long id) {
        return RestaurantDTO.fromEntity(service.getRestaurantById(id));
    }
    @GetMapping("/city/{cityId}")
    public List<RestaurantDTO> getRestaurantByCityId(@PathVariable Long cityId) {
        List<Restaurant> restaurants = service.getRestaurantsByCityId(cityId);
        return RestaurantDTO.fromEntityList(restaurants);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public RestaurantDTO createRestaurant(@Valid @RequestBody RestaurantDTO dto) {
        Restaurant createdRestaurant = service.createRestaurant(RestaurantDTO.toEntity(dto), dto.getCityName());
        return RestaurantDTO.fromEntity(createdRestaurant);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RestaurantDTO updateRestaurant(@PathVariable Long id, @Valid @RequestBody RestaurantDTO dto) {
        Restaurant restaurant = RestaurantDTO.toEntity(dto);
        Restaurant updatedRestaurant = service.updateRestaurant(id, restaurant, dto.getCityName());
        return RestaurantDTO.fromEntity(updatedRestaurant);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRestaurant(@PathVariable Long id) {
        service.deleteRestaurant(id);
    }
}