package pl.fishingwear.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminProductController {

    @GetMapping("/produkty")
    public String productList() {
        return "admin/all-product";
    }

    @GetMapping("/produkty/nowy")
    public String productCreate() {
        return "admin/product-create";
    }
}
