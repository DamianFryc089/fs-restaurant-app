package fun.kociarnia.bazy_danych_projekt.restaurant;


import fun.kociarnia.bazy_danych_projekt.city.City;
import fun.kociarnia.bazy_danych_projekt.city.CityRepository;
import fun.kociarnia.bazy_danych_projekt.exception.NotFoundException;
import fun.kociarnia.bazy_danych_projekt.exception.ResourceAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RestaurantService {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantService.class);

    private final RestaurantRepository restaurantRepository;
    private final CityRepository cityRepository;

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant", "id", id));
    }

    public Restaurant createRestaurant(Restaurant restaurant, String cityName) {
        restaurantRepository.findByName(restaurant.getName()).ifPresent(existingRestaurant -> {
            throw new ResourceAlreadyExistsException("Restaurant with name " + restaurant.getName() + " already exists");
        });
        Restaurant newRestaurant = new Restaurant();
        newRestaurant.setName(restaurant.getName());
        newRestaurant.setStreet(restaurant.getStreet());
        newRestaurant.setPhone(restaurant.getPhone());
        City city = cityRepository.findByName(cityName)
                .orElseThrow(() -> new NotFoundException("City", "name", cityName));
        newRestaurant.setCity(city);

        Restaurant savedRestaurant = restaurantRepository.save(newRestaurant);
        logger.info("Restaurant created: id={}, name={}, city={}",
                savedRestaurant.getId(), savedRestaurant.getName(), cityName);
        return savedRestaurant;
    }

    public Restaurant updateRestaurant(Long id, Restaurant updatedRestaurant, String cityName) {
        City city = cityRepository.findByName(cityName)
                .orElseThrow(() -> new NotFoundException("City", "name", cityName));
        Restaurant restaurant = getRestaurantById(id);
        restaurant.setName(updatedRestaurant.getName());
        restaurant.setStreet(updatedRestaurant.getStreet());
        restaurant.setPhone(updatedRestaurant.getPhone());
        restaurant.setCity(city);

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        logger.info("Restaurant updated: id={}, name={}, city={}",
                savedRestaurant.getId(), savedRestaurant.getName(), cityName);
        return savedRestaurant;
    }

    public void deleteRestaurant(Long id) {
        Restaurant restaurant = getRestaurantById(id);
        restaurantRepository.deleteById(id);
        logger.info("Restaurant deleted: id={}, name={}", restaurant.getId(), restaurant.getName());
    }

    public List<Restaurant> getRestaurantsByCityId(Long cityId) {
        return restaurantRepository.findByCity_Id(cityId);
    }
}