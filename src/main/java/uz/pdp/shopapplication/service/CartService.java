package uz.pdp.shopapplication.service;


import uz.pdp.shopapplication.dto.CartDto;
import uz.pdp.shopapplication.dto.UserDto;
import uz.pdp.shopapplication.entity.Cart;
import uz.pdp.shopapplication.entity.CartItem;

import java.util.List;

public interface CartService {

    CartDto getUserCart(UserDto userDto);

    void addToCart(UserDto userDto, Long productId, int quantity);

    void removeFromCart(UserDto userDto, Long productId);

    void clearCart(UserDto userDto);

}
