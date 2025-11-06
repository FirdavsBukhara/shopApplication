package uz.pdp.shopapplication.service;

import uz.pdp.shopapplication.dto.OrderDto;
import uz.pdp.shopapplication.dto.OrderSummaryDto;

import java.util.List;

public interface OrderService {

    OrderDto placeOrder();

    List<OrderDto> getOrders();

    OrderSummaryDto getMyOrdersSummary();

}
