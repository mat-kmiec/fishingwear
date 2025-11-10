package pl.fishingwear.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {

    @GetMapping("/produkt")
    public String showProducts() {
        return "products/product";
    }
}
