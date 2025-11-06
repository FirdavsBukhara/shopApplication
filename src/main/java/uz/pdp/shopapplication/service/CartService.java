package uz.pdp.shopapplication.service;


import uz.pdp.shopapplication.dto.CartDto;
import uz.pdp.shopapplication.dto.UserDto;
import uz.pdp.shopapplication.entity.Cart;
import uz.pdp.shopapplication.entity.CartItem;

import java.util.List;

public interface CartService {

    CartDto getUserCart();

    void addToCart(Long productId, int quantity);

    void clearCart();

    void removeItem(Long itemId);
}
