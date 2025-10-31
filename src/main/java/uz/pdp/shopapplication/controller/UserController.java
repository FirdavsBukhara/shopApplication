package uz.pdp.shopapplication.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.shopapplication.dto.UserDto;
import uz.pdp.shopapplication.entity.User;
import uz.pdp.shopapplication.mapper.UserMapper;
import uz.pdp.shopapplication.service.DocxService;
import uz.pdp.shopapplication.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final DocxService docxService;

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getAllUsers().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/docx")
    public ResponseEntity<byte[]> downloadUserDocx(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserDto dto = UserMapper.toDto(user);

        byte[] docx = docxService.generateUserCertificate(dto);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user_" + id + ".docx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(docx);
    }
}
