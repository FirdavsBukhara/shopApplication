package uz.pdp.shopapplication.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.shopapplication.dto.CartDto;
import uz.pdp.shopapplication.dto.CartItemDto;
import uz.pdp.shopapplication.entity.Cart;
import uz.pdp.shopapplication.entity.CartItem;
import uz.pdp.shopapplication.entity.Product;
import uz.pdp.shopapplication.entity.User;
import uz.pdp.shopapplication.repository.CartRepository;
import uz.pdp.shopapplication.repository.OrderRepository;
import uz.pdp.shopapplication.repository.ProductRepository;
import uz.pdp.shopapplication.repository.UserRepository;
import uz.pdp.shopapplication.service.CartService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    private User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new RuntimeException("Неавторизованный доступ");
        }
        String username = auth.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void addToCart(Long productId, int quantity) {
        User user = getCurrentUser();

        Cart cart = user.getCart();
        if (cart == null) {
            cart = Cart.builder().user(user).items(new ArrayList<>()).build();
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existing = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst();

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem item = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();
            cart.getItems().add(item);
        }
        cartRepository.save(cart);
    }

    @Override
    public CartDto getUserCart() {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setItems(new ArrayList<>());
            return cartRepository.save(newCart);
        });

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            return CartDto.builder()
                    .items(new ArrayList<>())
                    .total(BigDecimal.ZERO)
                    .userBalance(user.getBalance() != null ? user.getBalance() : BigDecimal.ZERO)
                    .orderCount(orderRepository.countByUser(user))
                    .totalSpent(orderRepository.getTotalSpentByUser(user))
                    .build();
        }

        List<CartItemDto> items = cart.getItems().stream()
                .map(item -> new CartItemDto(
                        item.getId(),
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity()
                ))
                .toList();

        BigDecimal total = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartDto.builder()
                .items(items)
                .total(total)
                .userBalance(user.getBalance() != null ? user.getBalance() : BigDecimal.ZERO)
                .orderCount(orderRepository.countByUser(user))
                .totalSpent(orderRepository.getTotalSpentByUser(user))
                .build();
    }

    @Override
    public void removeItem(Long itemId) {
        User user = getCurrentUser();
        Cart cart = user.getCart();
        if (cart == null || cart.getItems() == null) return;

        boolean removed = cart.getItems().removeIf(item -> item.getId() != null && item.getId().equals(itemId));
        if (removed) {
            cartRepository.save(cart);
        }
    }

    @Override
    public void clearCart() {
        User user = getCurrentUser();
        Cart cart = user.getCart();
        if (cart == null) return;

        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
