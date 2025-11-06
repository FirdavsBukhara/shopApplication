package uz.pdp.shopapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.shopapplication.entity.Order;
import uz.pdp.shopapplication.entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order>findByUserId(Long userId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.user = :user")
    int countByUser(@Param("user") User user);

    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.user = :user")
    BigDecimal getTotalSpentByUser(@Param("user") User user);
}
