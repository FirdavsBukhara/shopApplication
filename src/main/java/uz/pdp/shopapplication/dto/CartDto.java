package uz.pdp.shopapplication.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {

    private List<CartItemDto> items;
    private BigDecimal total;
    private BigDecimal userBalance;
    private int orderCount;
    private BigDecimal totalSpent;

    public CartDto(List<CartItemDto> items) {
        this.items = items;
        this.total = calculateTotal(items);
    }

    private BigDecimal calculateTotal(List<CartItemDto> items) {
        if (items == null || items.isEmpty()) return BigDecimal.ZERO;
        return items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}