package pl.fishingwear.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.fishingwear.product.dto.ProductDto;
import pl.fishingwear.product.service.ProductService;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/produkt/{slug}")
    public String product(@PathVariable String slug, Model model) {
        ProductDto product = productService.getProductBySlug(slug);
        model.addAttribute("product", product);
        return "products/product";
    }
}
