package uz.pdp.shopapplication.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.shopapplication.dto.UserDto;
import uz.pdp.shopapplication.entity.User;
import uz.pdp.shopapplication.mapper.UserMapper;
import uz.pdp.shopapplication.repository.UserRepository;
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
    private final UserRepository userRepository;

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getAllUsers().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto>me(@AuthenticationPrincipal UserDetails ud){
        User user = userRepository.findByUsername(ud.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadUserPdf(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserDto dto = UserMapper.toDto(user);

//        byte[] docx = docxService.generateUserCertificate(dto);
        byte[] pdf = docxService.generateUserCertificatePdf(dto);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
