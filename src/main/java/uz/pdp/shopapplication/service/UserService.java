package uz.pdp.shopapplication.service;

import uz.pdp.shopapplication.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();
    User getUserById(Long id);
}
