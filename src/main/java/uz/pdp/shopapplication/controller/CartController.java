package uz.pdp.shopapplication.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import uz.pdp.shopapplication.dto.UserDto;
import uz.pdp.shopapplication.entity.User;
import uz.pdp.shopapplication.mapper.UserMapper;
import uz.pdp.shopapplication.repository.UserRepository;
import uz.pdp.shopapplication.service.CartService;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;


    private UserDto getCurrentUserDto(UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.toDto(user);
    }

    @GetMapping
    public ResponseEntity<?>getUserCart(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(cartService.getUserCart(getCurrentUserDto(userDetails)));
    }

    @PostMapping("/add")
    public ResponseEntity<?>addToCart(@AuthenticationPrincipal UserDetails userDetails,
                                      @RequestParam Long productId,
                                      @RequestParam(defaultValue = "1") int quantity){
        cartService.addToCart(getCurrentUserDto(userDetails), productId, quantity);
        return ResponseEntity.ok("Product added to the cart");
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?>removeFromCart(@AuthenticationPrincipal UserDetails userDetails,
                                           @PathVariable Long productId){
        cartService.removeFromCart(getCurrentUserDto(userDetails),productId);
        return ResponseEntity.ok("Product removed from the cart");
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?>clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        cartService.clearCart(getCurrentUserDto(userDetails));
        return ResponseEntity.ok("Cart cleared");
    }
}