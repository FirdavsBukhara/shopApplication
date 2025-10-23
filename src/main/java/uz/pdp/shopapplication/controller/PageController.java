package uz.pdp.shopapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // 🏠 Главная — редирект на index.html
    @GetMapping({"/", "/index"})
    public String home() {
        return "redirect:/index.html";
    }

    // 🔐 Страница логина
    @GetMapping("/login")
    public String loginPage() {
        return "redirect:/login.html";
    }

    // 🧾 Список товаров
    @GetMapping("/products")
    public String productsPage() {
        return "redirect:/products.html";
    }

    // ➕ Добавление товара (для ADMIN)
    @GetMapping("/addProduct")
    public String addProductPage() {
        return "redirect:/addProduct.html";
    }
}
