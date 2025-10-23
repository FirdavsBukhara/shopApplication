package uz.pdp.shopapplication.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.shopapplication.dto.JwtResponse;
import uz.pdp.shopapplication.dto.LoginRequest;
import uz.pdp.shopapplication.dto.RegisterRequest;
import uz.pdp.shopapplication.entity.Role;
import uz.pdp.shopapplication.entity.User;
import uz.pdp.shopapplication.repository.RoleRepository;
import uz.pdp.shopapplication.repository.UserRepository;
import uz.pdp.shopapplication.security.jwt.JwtTokenProvider;
import uz.pdp.shopapplication.service.AuthService;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public JwtResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken");
        }
        Role userRole = roleRepository.findByName(Role.RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role Not Found"));

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(userRole))
                .build();

        userRepository.save(user);
        String token = jwtTokenProvider.generateToken(user.getUsername());

        return new JwtResponse(token, user.getUsername());
    }

    @Override
    public JwtResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
        String token = jwtTokenProvider.generateToken(request.getUsername());
        return new JwtResponse(token, request.getUsername());
    }
}
