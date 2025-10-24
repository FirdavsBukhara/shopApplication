package uz.pdp.shopapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {


    @GetMapping({"/", "/index"})
    public String home() {
        return "redirect:/index.html";
    }


    @GetMapping("/login")
    public String loginPage() {
        return "redirect:/login.html";
    }


    @GetMapping("/products")
    public String productsPage() {
        return "redirect:/products.html";
    }


    @GetMapping("/addProduct")
    public String addProductPage() {
        return "redirect:/addProduct.html";
    }
}
