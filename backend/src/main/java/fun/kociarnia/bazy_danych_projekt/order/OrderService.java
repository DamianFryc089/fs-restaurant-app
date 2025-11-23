package fun.kociarnia.bazy_danych_projekt.order;


import fun.kociarnia.bazy_danych_projekt.exception.IllegalOperationException;
import fun.kociarnia.bazy_danych_projekt.exception.InvalidOrderStatusException;
import fun.kociarnia.bazy_danych_projekt.exception.NotFoundException;
import fun.kociarnia.bazy_danych_projekt.offer.Offer;
import fun.kociarnia.bazy_danych_projekt.offer.OfferRepository;
import fun.kociarnia.bazy_danych_projekt.user.User;
import fun.kociarnia.bazy_danych_projekt.user.UserRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final UserRepository userRepository;
    private final OfferRepository offerRepository;

    public OrderService(OrderRepository repository, UserRepository userRepository, OfferRepository offerRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.offerRepository = offerRepository;
    }

    public List<Order> getAllOrders() {
        return repository.findAll();
    }

    public Order getOrderById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order", "id", id));
    }

    public Order createOrder(Order order, Long clientId, Long offerId) {
        Order newOrder = new Order();
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("User", "id", clientId));
        newOrder.setClient(client);
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new NotFoundException("Offer", "id", offerId));

        if (order.getQuantity() <= 0 || order.getQuantity() > offer.getAvailableQuantity()) {
            throw new IllegalArgumentException("Quantity must be greater than zero and less than or equal to available quantity");
        }

        newOrder.setOffer(offer);
        newOrder.setQuantity(order.getQuantity());
        newOrder.setStatus(Order.Status.PENDING);
        BigDecimal total = offer.getPrice().multiply(BigDecimal.valueOf(newOrder.getQuantity()));
        newOrder.setTotalPrice(total);

        offer.setAvailableQuantity(offer.getAvailableQuantity() - newOrder.getQuantity());
        offerRepository.save(offer);
        return repository.save(newOrder);
    }

    public Order updateOrder(Long id, Order updatedOrder) {
        Order order = getOrderById(id);
        order.setStatus(updatedOrder.getStatus());
        return repository.save(order);
    }

    public Order completeOrder(Long id, Long employeeId) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("User", "id", employeeId));

        Order order = getOrderById(id);
        if (order.getStatus() != Order.Status.PENDING) {
            throw new InvalidOrderStatusException("Only pending orders can be confirmed");
        }

        if (!Objects.equals(employee.getRestaurant().getId(), order.getOffer().getRestaurant().getId()))
            throw new IllegalOperationException("Cannot complete order from other restaurant");

        order.setStatus(Order.Status.COMPLETED);
        return repository.save(order);
    }

    public Order cancelOrderEmployee(Long id, Long employeeId) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("User", "id", employeeId));

        Order order = getOrderById(id);
        if (order.getStatus() != Order.Status.PENDING) {
            throw new InvalidOrderStatusException("Only pending orders can be cancelled");
        }

        if (!Objects.equals(employee.getRestaurant().getId(), order.getOffer().getRestaurant().getId()))
            throw new IllegalOperationException("Cannot cancel order from other restaurant");


        Offer offer = order.getOffer();
        offer.setAvailableQuantity(offer.getAvailableQuantity() + order.getQuantity());
        offerRepository.save(offer);

        order.setStatus(Order.Status.CANCELLED);
        return repository.save(order);
    }

    public Order cancelOrderClient(Long id, Long clientId) {
        Order order = getOrderById(id);
        if (order.getStatus() != Order.Status.PENDING) {
            throw new InvalidOrderStatusException("Only pending orders can be cancelled");
        }

        if (!Objects.equals(clientId, order.getClient().getId()))
            throw new IllegalOperationException("Cannot cancel order that does not belong to you");

        Offer offer = order.getOffer();
        offer.setAvailableQuantity(offer.getAvailableQuantity() + order.getQuantity());
        offerRepository.save(offer);

        order.setStatus(Order.Status.CANCELLED);
        return repository.save(order);
    }

    public void deleteOrder(Long id) {
        repository.deleteById(id);
    }

    public List<Order> getOrdersByClient(Long clientId) {
        return repository.findByClientIdOrderByCreatedAtDesc(clientId);
    }

    public List<Order> getOrdersByRestaurant(Long restaurantId, Long employeeId) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("User", "id", employeeId));

        if (!Objects.equals(employee.getRestaurant().getId(), restaurantId))
            throw new IllegalOperationException("Cannot fetch orders from other restaurant");

        return repository.findByOffer_Restaurant_Id(restaurantId);
    }
}