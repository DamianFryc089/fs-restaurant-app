package fun.kociarnia.bazy_danych_projekt.city;


import fun.kociarnia.bazy_danych_projekt.exception.IllegalOperationException;
import fun.kociarnia.bazy_danych_projekt.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CityService {

    private static final Logger logger = LoggerFactory.getLogger(CityService.class);

    private final CityRepository cityRepository;

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public City getCityById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("City", "id", id));
    }

    public City createCity(City city) {
        cityRepository.findByName(city.getName()).ifPresent(existingCity -> {
            throw new IllegalOperationException("City name already used.");
        });

        City savedCity = cityRepository.save(city);
        logger.info("City created: cityId={}, name={}", savedCity.getId(), savedCity.getName());
        return savedCity;
    }
}
