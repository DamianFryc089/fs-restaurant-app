package fun.kociarnia.bazy_danych_projekt.city;


import fun.kociarnia.bazy_danych_projekt.exception.IllegalOperationException;
import fun.kociarnia.bazy_danych_projekt.exception.NotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
public class CityService {

    private final CityRepository repository;

    public CityService(CityRepository repository) {
        this.repository = repository;
    }

    public List<City> getAllCities() {
        return repository.findAll();
    }

    public City getCityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("City", "id", id));
    }

    public City createCity(City city) {
        repository.findByName(city.getName()).ifPresent(existingCity -> {
            throw new IllegalOperationException("City name already used.");
        });
        return repository.save(city);
    }
}
