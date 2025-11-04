package uz.pdp.shopapplication.service;

import uz.pdp.shopapplication.dto.OrderDto;

import java.util.List;

public interface OrderService {

    OrderDto placeOrder();
    List<OrderDto> getOrders();
}
