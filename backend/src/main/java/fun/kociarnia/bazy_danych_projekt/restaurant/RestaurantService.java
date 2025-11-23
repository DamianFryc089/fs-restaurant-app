package fun.kociarnia.bazy_danych_projekt.restaurant;


import fun.kociarnia.bazy_danych_projekt.city.City;
import fun.kociarnia.bazy_danych_projekt.city.CityRepository;
import fun.kociarnia.bazy_danych_projekt.exception.NotFoundException;
import fun.kociarnia.bazy_danych_projekt.exception.ResourceAlreadyExistsException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository repository;
    private final CityRepository cityRepository;

    public RestaurantService(RestaurantRepository repository, CityRepository cityRepository) {
        this.repository = repository;
        this.cityRepository = cityRepository;
    }

    public List<Restaurant> getAllRestaurants() {
        return repository.findAll();
    }

    public Restaurant getRestaurantById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant", "id", id));
    }

    public Restaurant createRestaurant(Restaurant restaurant, String cityName) {
        repository.findByName(restaurant.getName()).ifPresent(existingRestaurant -> {
            throw new ResourceAlreadyExistsException("Restaurant with name " + restaurant.getName() + " already exists");
        });
        Restaurant newRestaurant = new Restaurant();
        newRestaurant.setName(restaurant.getName());
        newRestaurant.setStreet(restaurant.getStreet());
        newRestaurant.setPhone(restaurant.getPhone());
        City city = cityRepository.findByName(cityName)
                .orElseThrow(() -> new NotFoundException("City", "name", cityName));
        newRestaurant.setCity(city);
        return repository.save(newRestaurant);
    }

    public Restaurant updateRestaurant(Long id, Restaurant updatedRestaurant, String cityName) {
        City city = cityRepository.findByName(cityName)
                .orElseThrow(() -> new NotFoundException("City", "name", cityName));
        Restaurant restaurant = getRestaurantById(id);
        restaurant.setName(updatedRestaurant.getName());
        restaurant.setStreet(updatedRestaurant.getStreet());
        restaurant.setPhone(updatedRestaurant.getPhone());
        restaurant.setCity(city);
        return repository.save(restaurant);
    }

    public void deleteRestaurant(Long id) {
        repository.deleteById(id);
    }

    public List<Restaurant> getRestaurantsByCityId(Long cityId) {
        return repository.findByCity_Id(cityId);
    }
}