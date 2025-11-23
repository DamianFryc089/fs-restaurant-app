package fun.kociarnia.bazy_danych_projekt.city;


import fun.kociarnia.bazy_danych_projekt.city.dto.CityDTO;
import fun.kociarnia.bazy_danych_projekt.offer.Offer;
import fun.kociarnia.bazy_danych_projekt.offer.dto.OfferDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bazy/cities")
public class CityController {

    private final CityService service;

    public CityController(CityService service) {
        this.service = service;
    }

    @GetMapping
    public List<CityDTO> getAllCities() {
        List<City> cities = service.getAllCities();
        return CityDTO.fromEntityList(cities);
    }

    @GetMapping("/{id}")
    public CityDTO getCityById(@PathVariable Long id) {
        return CityDTO.fromEntity(service.getCityById(id));
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public CityDTO createCity(@RequestBody CityDTO dto)
    {
        City city = CityDTO.toEntity(dto);
        return CityDTO.fromEntity(service.createCity(city));
    }
}