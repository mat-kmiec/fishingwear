package pl.fishingwear.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminCategoryController {
    @GetMapping("/kategorie/nowa")
    public String categoryCreate() {
        return "admin/create-category";
    }
}
