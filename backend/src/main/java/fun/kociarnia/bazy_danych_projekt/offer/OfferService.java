package fun.kociarnia.bazy_danych_projekt.offer;


import fun.kociarnia.bazy_danych_projekt.exception.IllegalOperationException;
import fun.kociarnia.bazy_danych_projekt.exception.NotFoundException;
import fun.kociarnia.bazy_danych_projekt.restaurant.Restaurant;
import fun.kociarnia.bazy_danych_projekt.restaurant.RestaurantRepository;
import fun.kociarnia.bazy_danych_projekt.user.User;
import fun.kociarnia.bazy_danych_projekt.user.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OfferService {

    private final OfferRepository repository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public OfferService(OfferRepository repository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    public List<Offer> getOffers() {
        return repository.findAll();
    }

    public List<Offer> getOffersByStatus(Offer.Status status) {
        return repository.findAllByStatus(status);
    }

    public Offer getOfferById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Offer", "id", id));
    }

    public Offer createOffer(Offer offer, Long restaurantId, Long employeeId) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("User", "id", employeeId));

        if (!Objects.equals(employee.getRestaurant().getId(), restaurantId))
            throw new IllegalOperationException("Cannot create an offer for other restaurant");

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant", "id", restaurantId));
        offer.setRestaurant(restaurant);
        return repository.save(offer);
    }
    public Offer updateOffer(Long id, Offer updatedOffer) {
        Offer offer = getOfferById(id);
        offer.setTitle(updatedOffer.getTitle());
        offer.setDescription(updatedOffer.getDescription());
        offer.setPrice(updatedOffer.getPrice());
        offer.setAvailableQuantity(updatedOffer.getAvailableQuantity());
        offer.setStatus(updatedOffer.getStatus());
        return repository.save(offer);
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
        return repository.save(offer);
    }

    public void deleteOffer(Long id) {
        repository.deleteById(id);
    }

    public List<Offer> getOffersByRestaurant(Long restaurantId) {
        Optional<List<Offer>> offers = repository.findByRestaurantId(restaurantId);
        return offers.orElseGet(List::of);
    }

    public List<Offer> getOffersByCityName(String cityName) {
        return repository.findByRestaurant_City_NameIgnoreCase(cityName);
    }
    public List<Offer> getOffersByCityId(Long cityId) {
        return repository.findByRestaurant_City_Id(cityId);
    }

}