package fun.kociarnia.bazy_danych_projekt.city.dto;


import fun.kociarnia.bazy_danych_projekt.city.City;
import fun.kociarnia.bazy_danych_projekt.offer.Offer;
import fun.kociarnia.bazy_danych_projekt.offer.dto.OfferDTO;
import fun.kociarnia.bazy_danych_projekt.user.User;
import fun.kociarnia.bazy_danych_projekt.user.dto.UserDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
public class CityDTO {
    private Long id;
    private String name;
    private String postalCode;

    public static CityDTO fromEntity(City city) {
        CityDTO dto = new CityDTO();
        dto.setId(city.getId());
        dto.setName(city.getName());
        dto.setPostalCode(city.getPostalCode());
        return dto;
    }

    public static List<CityDTO> fromEntityList(List<City> cities) {
        return cities.stream()
                .map(CityDTO::fromEntity)
                .toList();
    }

    public static City toEntity(CityDTO dto) {
        City city = new City();
        city.setName(dto.getName());
        city.setPostalCode(dto.getPostalCode());
        return city;
    }
}
