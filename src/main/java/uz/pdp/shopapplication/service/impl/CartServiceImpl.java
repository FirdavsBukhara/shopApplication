package uz.pdp.shopapplication.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.shopapplication.dto.CartDto;
import uz.pdp.shopapplication.dto.CartItemDto;
import uz.pdp.shopapplication.dto.UserDto;
import uz.pdp.shopapplication.entity.Cart;
import uz.pdp.shopapplication.entity.CartItem;
import uz.pdp.shopapplication.entity.Product;
import uz.pdp.shopapplication.entity.User;
import uz.pdp.shopapplication.repository.CartRepository;
import uz.pdp.shopapplication.repository.ProductRepository;
import uz.pdp.shopapplication.repository.UserRepository;
import uz.pdp.shopapplication.service.CartService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    @Override
    public CartDto getUserCart(UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).
                orElseThrow(() -> new RuntimeException("User Not Found"));

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = Cart.builder().user(user).build();
            return cartRepository.save(newCart);
        });

        List<CartItemDto> items = cart.getItems().stream()
                .map(item -> new CartItemDto(
                        item.getId(),
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity()))
                .collect(Collectors.toList());

        return CartDto.builder()
                .id(cart.getId())
                .username(user.getUsername())
                .items(items)
                .build();
    }

    @Override
    public void addToCart(UserDto userDto, Long productId, int quantity) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user).
                orElseGet(() -> cartRepository.save(Cart.builder().user(user).build()));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem existing = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();

            cart.getItems().add(newItem);
        }
        cartRepository.save(cart);
    }

    @Override
    public void removeFromCart(UserDto userDto, Long productId) {

        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().removeIf(i -> i.getProduct().getId().equals(productId));
        cartRepository.save(cart);
    }


    @Override
    public void clearCart(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
