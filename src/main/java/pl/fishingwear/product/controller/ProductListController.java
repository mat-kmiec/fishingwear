package pl.fishingwear.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductListController {

    @GetMapping("/lista-produktow")
    public String showProductList() {
        return "products/product-list";
    }
}
