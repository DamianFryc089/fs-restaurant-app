package fun.kociarnia.bazy_danych_projekt.offer;


import fun.kociarnia.bazy_danych_projekt.exception.IllegalOperationException;
import fun.kociarnia.bazy_danych_projekt.exception.NotFoundException;
import fun.kociarnia.bazy_danych_projekt.restaurant.Restaurant;
import fun.kociarnia.bazy_danych_projekt.restaurant.RestaurantRepository;
import fun.kociarnia.bazy_danych_projekt.user.User;
import fun.kociarnia.bazy_danych_projekt.user.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OfferService {

    private static final Logger logger = LoggerFactory.getLogger(OfferService.class);

    private final OfferRepository offerRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public List<Offer> getOffers() {
        return offerRepository.findAll();
    }

    public List<Offer> getOffersByStatus(Offer.Status status) {
        return offerRepository.findAllByStatus(status);
    }

    public Offer getOfferById(Long id) {
        return offerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Offer", "id", id));
    }

    public Offer createOffer(Offer offer, Long restaurantId, Long employeeId) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("User", "id", employeeId));

        if (!Objects.equals(employee.getRestaurant().getId(), restaurantId))
            throw new IllegalOperationException("Cannot create an offer for other restaurant");

        if (offer.getPrice().compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalOperationException("Price of an offer cannot be lower than 0");

        if (offer.getAvailableQuantity() < 1)
            throw new IllegalOperationException("Available quantity of an offer cannot be lower than 1");

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant", "id", restaurantId));
        offer.setRestaurant(restaurant);
        Offer savedOffer = offerRepository.save(offer);
        logger.info("Offer created: offerId={}, restaurantId={}, employeeId={}, title={}, price={}, quantity={}",
                savedOffer.getId(), restaurantId, employeeId, savedOffer.getTitle(),
                savedOffer.getPrice(), savedOffer.getAvailableQuantity());
        return savedOffer;
    }

    public Offer updateOffer(Long id, Offer updatedOffer, Long restaurantId) {
        Offer offer = getOfferById(id);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant", "id", restaurantId));
        offer.setRestaurant(restaurant);
        offer.setTitle(updatedOffer.getTitle());
        offer.setDescription(updatedOffer.getDescription());
        offer.setPrice(updatedOffer.getPrice());
        offer.setAvailableQuantity(updatedOffer.getAvailableQuantity());
        offer.setStatus(updatedOffer.getStatus());

        Offer savedOffer = offerRepository.save(offer);
        logger.info("Offer updated with restaurant change: offerId={}, restaurantId={}, title={}, price={}, quantity={}, status={}",
                savedOffer.getId(), restaurantId, savedOffer.getTitle(),
                savedOffer.getPrice(), savedOffer.getAvailableQuantity(),
                savedOffer.getStatus());
        return savedOffer;
    }

    public void deleteOffer(Long id) {
        Offer offer = getOfferById(id);
        offerRepository.deleteById(id);
        logger.info("Offer deleted: offerId={}, restaurantId={}, title={}",
                offer.getId(), offer.getRestaurant().getId(), offer.getTitle());
    }

    public List<Offer> getOffersByRestaurant(Long restaurantId) {
        Optional<List<Offer>> offers = offerRepository.findByRestaurantId(restaurantId);
        return offers.orElseGet(List::of);
    }

    public List<Offer> getOffersByCityName(String cityName) {
        return offerRepository.findByRestaurant_City_NameIgnoreCase(cityName);
    }
    public List<Offer> getOffersByCityId(Long cityId) {
        return offerRepository.findByRestaurant_City_Id(cityId);
    }

}