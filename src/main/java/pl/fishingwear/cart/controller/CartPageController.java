package pl.fishingwear.cart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.fishingwear.cart.dto.CartViewDto;
import pl.fishingwear.cart.service.CartService;

@Controller
@RequiredArgsConstructor
public class CartPageController {

    private final CartService cartService;

    @GetMapping("/koszyk")
    public String showCartPage(Model model) {
        CartViewDto cartView = cartService.getCurrentCartView();

        model.addAttribute("items", cartView.items());
        model.addAttribute("total", cartView.subtotalPrice());

        return "cart/cart";
    }
}
