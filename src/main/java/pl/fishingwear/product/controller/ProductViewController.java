package pl.fishingwear.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.fishingwear.product.dto.ProductResponseDto;
import pl.fishingwear.product.service.ProductService;

@Controller
@RequestMapping("/produkt")
@RequiredArgsConstructor
public class ProductViewController {

    private final ProductService productService;

    @GetMapping("/{slug}")
    public String showProduct(@PathVariable String slug, Model model) {
        ProductResponseDto product = productService.getProductBySlug(slug);
        model.addAttribute("product", product);
        return "products/product"; // templates/products/product.html
    }
}