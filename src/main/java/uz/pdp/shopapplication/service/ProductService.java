package uz.pdp.shopapplication.service;

import uz.pdp.shopapplication.dto.ProductDto;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(ProductDto dto);

    ProductDto updateProduct(Long id, ProductDto dto);

    void deleteProduct(Long id);

    ProductDto getProductById(Long id);

    List<ProductDto> getAllProducts();

    List<ProductDto> getProductsByCategory(Long categoryId);
}
