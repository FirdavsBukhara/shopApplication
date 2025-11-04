package uz.pdp.shopapplication.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.shopapplication.dto.OrderDto;
import uz.pdp.shopapplication.dto.OrderItemDto;
import uz.pdp.shopapplication.entity.Cart;
import uz.pdp.shopapplication.entity.Order;
import uz.pdp.shopapplication.entity.OrderItem;
import uz.pdp.shopapplication.entity.User;
import uz.pdp.shopapplication.repository.CartRepository;
import uz.pdp.shopapplication.repository.OrderRepository;
import uz.pdp.shopapplication.repository.UserRepository;
import uz.pdp.shopapplication.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    @Transactional
    @Override
    public OrderDto placeOrder() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = user.getCart();
        if (cart == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        BigDecimal total = cart.getItems().stream()
                .map(i -> i.getProduct().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (user.getBalance().compareTo(total) < 0) {
            throw new RuntimeException("User balance is less than total price");
        }

        user.setBalance(user.getBalance().subtract(total));

        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalPrice(total);

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(i -> OrderItem.builder()
                        .order(order)
                        .product(i.getProduct())
                        .quantity(i.getQuantity())
                        .price(i.getProduct().getPrice())
                        .build())
                .collect(Collectors.toList());

        order.setItems(orderItems);

        orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        userRepository.save(user);

        return new OrderDto(
                order.getId(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                order.getItems().stream()
                        .map(i -> new OrderItemDto(
                                i.getProduct().getName(),
                                i.getQuantity(),
                                i.getPrice()))
                        .collect(Collectors.toList()));
    }

    @Override
    public List<OrderDto> getOrders() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUserId(user.getId()).stream()
                .map(order -> new OrderDto(
                        order.getId(),
                        order.getTotalPrice(),
                        order.getCreatedAt(),
                        order.getItems().stream()
                                .map(i -> new OrderItemDto(
                                        i.getProduct().getName(),
                                        i.getQuantity(),
                                        i.getPrice()))
                                .collect(Collectors.toList())
                )).collect(Collectors.toList());
    }
}
