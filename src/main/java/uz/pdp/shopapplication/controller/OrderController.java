package uz.pdp.shopapplication.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.shopapplication.dto.OrderDto;
import uz.pdp.shopapplication.entity.Order;
import uz.pdp.shopapplication.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<OrderDto> placeOrder() {
        return ResponseEntity.ok(orderService.placeOrder());
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderDto>> getOrders() {
        return ResponseEntity.ok(orderService.getOrders());
    }

}
