package uz.pdp.shopapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.shopapplication.entity.Cart;
import uz.pdp.shopapplication.entity.CartItem;
import uz.pdp.shopapplication.entity.User;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
