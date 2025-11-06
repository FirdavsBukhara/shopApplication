package uz.pdp.shopapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSummaryDto {

    private Long ordersCount;
    private BigDecimal totalSpent;

}
