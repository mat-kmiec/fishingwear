package pl.fishingwear.common.attributes;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.fishingwear.product.model.Category;
import pl.fishingwear.product.service.CategoryService;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {

    private final CategoryService categoryService;

    @ModelAttribute("category")
    public List<Category> globalCategory() {
        return categoryService.getAllCategories();
    }
}