package uz.pdp.shopapplication.controller;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Void> redirectToLogin() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/login.html");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);

    }
}
