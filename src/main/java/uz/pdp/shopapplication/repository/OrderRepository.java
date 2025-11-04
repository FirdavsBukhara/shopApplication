package uz.pdp.shopapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.shopapplication.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order>findByUserId(Long userId);
}
