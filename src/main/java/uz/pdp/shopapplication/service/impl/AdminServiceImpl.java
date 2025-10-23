package uz.pdp.shopapplication.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.shopapplication.dto.AdminStatsDto;
import uz.pdp.shopapplication.repository.CategoryRepository;
import uz.pdp.shopapplication.repository.ProductRepository;
import uz.pdp.shopapplication.repository.UserRepository;
import uz.pdp.shopapplication.service.AdminService;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {


    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public AdminStatsDto getStats() {
        long users = userRepository.count();
        long categories = categoryRepository.count();
        long products = productRepository.count();
        return new AdminStatsDto(users, categories, products);
    }
}
