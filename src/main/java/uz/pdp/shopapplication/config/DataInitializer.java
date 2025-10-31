package uz.pdp.shopapplication.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.pdp.shopapplication.entity.Role;
import uz.pdp.shopapplication.repository.RoleRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void initRoles() {
        if (roleRepository.findByName(Role.RoleName.ROLE_ADMIN).isEmpty()) {
            roleRepository.save(Role.builder().name(Role.RoleName.ROLE_ADMIN).build());
            System.out.println("✅ Создана роль ROLE_ADMIN");
        }

        if (roleRepository.findByName(Role.RoleName.ROLE_USER).isEmpty()) {
            roleRepository.save(Role.builder().name(Role.RoleName.ROLE_USER).build());
            System.out.println("✅ Создана роль ROLE_USER");
        }
    }
}
