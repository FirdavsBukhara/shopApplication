package uz.pdp.shopapplication.dto;

import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CategoryDto {

    private Long id;
    private String name;
    List<ProductShortDto> products;
}
