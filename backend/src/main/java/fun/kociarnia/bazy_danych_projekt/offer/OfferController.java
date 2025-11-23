package fun.kociarnia.bazy_danych_projekt.offer;


import fun.kociarnia.bazy_danych_projekt.MyUserDetails;
import fun.kociarnia.bazy_danych_projekt.offer.dto.OfferDTO;
import fun.kociarnia.bazy_danych_projekt.restaurant.Restaurant;
import fun.kociarnia.bazy_danych_projekt.user.User;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bazy/offers")
public class OfferController {

    private final OfferService service;

    public OfferController(OfferService service) {
        this.service = service;
    }

    @GetMapping
    public List<OfferDTO> getOffers() {
        List<Offer> offers = service.getOffers();
        return OfferDTO.fromEntityList(offers);
    }

    @GetMapping("/active")
    public List<OfferDTO> getAllActiveOffers() {
        List<Offer> offers = service.getOffersByStatus(Offer.Status.ACTIVE);
        return OfferDTO.fromEntityList(offers);
    }

    @GetMapping("/{id}")
    public OfferDTO getOfferById(@PathVariable Long id) {
        return OfferDTO.fromEntity(service.getOfferById(id));
    }

    @GetMapping("/city/{cityId}")
    public List<OfferDTO> getOfferByCityId(@PathVariable Long cityId) {
        List<Offer> offers = service.getOffersByCityId(cityId);
        return OfferDTO.fromEntityList(offers);
    }

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public OfferDTO createOffer(@Valid @RequestBody OfferDTO dto, @AuthenticationPrincipal MyUserDetails currentUser) {
        Offer offer = OfferDTO.toEntity(dto);
        return OfferDTO.fromEntity(service.createOffer(offer, dto.getRestaurantId(), currentUser.getId()));
    }

//    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('EMPLOYEE')")
//    public OfferDTO updateOffer(@PathVariable Long id, @Valid @RequestBody OfferDTO dto) {
//        Offer offer = OfferDTO.toEntity(dto);
//        return OfferDTO.fromEntity(service.updateOffer(id, offer, dto.getRestaurantId()));
//    }

//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public void deleteOffer(@PathVariable Long id) {
//        service.deleteOffer(id);
//    }

    @GetMapping("/restaurant/{restaurantId}")
    public List<OfferDTO> getOffersByRestaurant(@PathVariable Long restaurantId) {
        List<Offer> offers = service.getOffersByRestaurant(restaurantId);
        return OfferDTO.fromEntityList(offers);
    }
}