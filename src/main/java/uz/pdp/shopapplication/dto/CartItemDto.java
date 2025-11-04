package uz.pdp.shopapplication.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {

    private Long id;
    private String productName;
    private BigDecimal price;
    private int quantity;
}
