package pl.fishingwear.common.attributes;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.fishingwear.cart.service.CartService;
import pl.fishingwear.product.model.Category;
import pl.fishingwear.product.service.CategoryService;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {

    private final CategoryService categoryService;
    private final CartService cartService;

    @ModelAttribute("category")
    public List<Category> globalCategory() {
        return categoryService.getAllCategories();
    }

    @ModelAttribute("cartItemCount")
    public int globalCartItemCount() {
        return cartService.getCartItemCount();
    }
}