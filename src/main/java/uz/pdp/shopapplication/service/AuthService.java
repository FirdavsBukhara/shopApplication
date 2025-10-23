package uz.pdp.shopapplication.service;

import uz.pdp.shopapplication.dto.JwtResponse;
import uz.pdp.shopapplication.dto.LoginRequest;
import uz.pdp.shopapplication.dto.RegisterRequest;

public interface AuthService {

    JwtResponse register(RegisterRequest request);

    JwtResponse login(LoginRequest request);
}
