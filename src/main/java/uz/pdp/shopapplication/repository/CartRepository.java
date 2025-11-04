package uz.pdp.shopapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.shopapplication.entity.Cart;
import uz.pdp.shopapplication.entity.User;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
