package fun.kociarnia.bazy_danych_projekt.order;


import fun.kociarnia.bazy_danych_projekt.MyUserDetails;
import fun.kociarnia.bazy_danych_projekt.order.dto.OrderDTO;
import fun.kociarnia.bazy_danych_projekt.user.User;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = service.getAllOrders();
        return OrderDTO.fromEntityList(orders);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderDTO getOrderById(@PathVariable Long id) {
        return OrderDTO.fromEntity(service.getOrderById(id));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated() and hasRole('CLIENT')")
    public OrderDTO createOrder(@Valid @RequestBody OrderDTO dto, @AuthenticationPrincipal MyUserDetails currentUser) {
        Order order = OrderDTO.toEntity(dto);
        Order createdOrder = service.createOrder(order, currentUser.getId(), dto.getOfferId());
        return OrderDTO.fromEntity(createdOrder);
    }

//    @PutMapping("/{id}")
//    public OrderDTO updateOrder(@PathVariable Long id, @Valid @RequestBody OrderDTO dto) {
//        return OrderDTO.fromEntity(service.updateOrder(id, OrderDTO.toEntity(dto)));
//    }

    @PutMapping("/{id}/completed")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public OrderDTO completeOrder(@PathVariable Long id, @AuthenticationPrincipal MyUserDetails currentUser) {
        return OrderDTO.fromEntity(service.completeOrder(id, currentUser.getId()));
    }

    @PutMapping("/{id}/canceled")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('CLIENT')")
    public OrderDTO cancelOrder(@PathVariable Long id, @AuthenticationPrincipal MyUserDetails currentUser) {
        Order order = currentUser.getRole() == User.Role.CLIENT
                ? service.cancelOrderClient(id, currentUser.getId())
                : service.cancelOrderEmployee(id, currentUser.getId());
        return OrderDTO.fromEntity(order);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteOrder(@PathVariable Long id) {
        service.deleteOrder(id);
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasRole('CLIENT') and #clientId == authentication.principal.id")
    public List<OrderDTO> getOrdersByClient(@PathVariable Long clientId) {
        List<Order> orders = service.getOrdersByClient(clientId);
        return OrderDTO.fromEntityList(orders);
    }

    @GetMapping("/restaurant/{restaurantId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public List<OrderDTO> getOrdersByRestaurant(@PathVariable Long restaurantId, @AuthenticationPrincipal MyUserDetails currentUser) {
        List<Order> orders = service.getOrdersByRestaurant(restaurantId, currentUser.getId());
        return OrderDTO.fromEntityList(orders);
    }
}