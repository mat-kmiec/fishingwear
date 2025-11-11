package pl.fishingwear.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.fishingwear.product.dto.ProductDto;
import pl.fishingwear.product.service.ColorService;
import pl.fishingwear.product.service.ProductService;
import pl.fishingwear.product.service.SizeService;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/produkty")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ColorService colorService;
    private final SizeService sizeService;

    @GetMapping("/{slug}")
    public String getProduct(@PathVariable String slug, Model model) {
        ProductDto product = productService.getProductBySlug(slug);
        model.addAttribute("product", product);
        return "products/product";
    }


    @GetMapping
    public String getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String categorySlug,
            @RequestParam(required = false) List<String> color,
            @RequestParam(required = false) List<String> sizeFilter,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Model model) {

        Page<ProductDto> productPage = productService.getFilteredProducts(
                page, size, sortBy, sortDir, categorySlug,
                color, sizeFilter, search, minPrice, maxPrice
        );

        model.addAttribute("productPage", productPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("sizes", sizeService.getAllSizes());
        model.addAttribute("colors", colorService.getAllColors());
        model.addAttribute("selectedSizes", sizeFilter);
        model.addAttribute("selectedColors", color);
        model.addAttribute("selectedCategory", categorySlug);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        return "products/product-list";
    }
}
