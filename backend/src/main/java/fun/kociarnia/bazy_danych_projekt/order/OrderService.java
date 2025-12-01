package fun.kociarnia.bazy_danych_projekt.order;


import fun.kociarnia.bazy_danych_projekt.exception.IllegalOperationException;
import fun.kociarnia.bazy_danych_projekt.exception.InvalidOrderStatusException;
import fun.kociarnia.bazy_danych_projekt.exception.NotFoundException;
import fun.kociarnia.bazy_danych_projekt.offer.Offer;
import fun.kociarnia.bazy_danych_projekt.offer.OfferRepository;
import fun.kociarnia.bazy_danych_projekt.user.User;
import fun.kociarnia.bazy_danych_projekt.user.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OfferRepository offerRepository;


    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
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

        Order savedOrder = orderRepository.save(newOrder);
        logger.info("Order created: orderId={}, clientId={}, offerId={}, quantity={}, totalPrice={}",
                savedOrder.getId(), clientId, offerId, savedOrder.getQuantity(), savedOrder.getTotalPrice());
        return savedOrder;
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
        Order savedOrder = orderRepository.save(order);
        logger.info("Order completed: orderId={}, employeeId={}, clientId={}",
                savedOrder.getId(), employeeId, savedOrder.getClient().getId());
        return savedOrder;
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
        Order savedOrder = orderRepository.save(order);
        logger.info("Order cancelled by employee: orderId={}, employeeId={}, clientId={}",
                savedOrder.getId(), employeeId, savedOrder.getClient().getId());
        return savedOrder;
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
        Order savedOrder = orderRepository.save(order);
        logger.info("Order cancelled by client: orderId={}, clientId={}",
                savedOrder.getId(), clientId);
        return savedOrder;
    }

    public void deleteOrder(Long id) {
        Order order = getOrderById(id);
        orderRepository.deleteById(id);
        logger.info("Order deleted: orderId={}, clientId={}, offerId={}",
                order.getId(), order.getClient().getId(), order.getOffer().getId());
    }

    public List<Order> getOrdersByClient(Long clientId) {
        return orderRepository.findByClientIdOrderByCreatedAtDesc(clientId);
    }

    public List<Order> getOrdersByRestaurant(Long restaurantId, Long employeeId) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("User", "id", employeeId));

        if (!Objects.equals(employee.getRestaurant().getId(), restaurantId))
            throw new IllegalOperationException("Cannot fetch orders from other restaurant");

        return orderRepository.findByOffer_Restaurant_Id(restaurantId);
    }
}