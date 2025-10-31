package uz.pdp.shopapplication.mapper;

import uz.pdp.shopapplication.dto.UserDto;
import uz.pdp.shopapplication.entity.User;

public class UserMapper {

    public static UserDto toDto(User user) {
        if(user == null) { return null; }

        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getUsername())
                .passportNumber("AB1234567")
                .balance(2200.50)
                .build();
    }
}
