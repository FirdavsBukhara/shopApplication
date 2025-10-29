package uz.pdp.shopapplication.service;

import uz.pdp.shopapplication.dto.ProductDto;

public interface PdfService {
    byte[] generateProductPdf(ProductDto product);

}
