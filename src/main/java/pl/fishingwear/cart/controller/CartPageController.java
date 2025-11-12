package pl.fishingwear.cart.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.fishingwear.cart.dto.CartViewDto;
import pl.fishingwear.order.dto.OrderConfirmationDto;
import pl.fishingwear.cart.service.CartService;
import pl.fishingwear.order.service.OrderService;
import pl.fishingwear.user.model.User;
import pl.fishingwear.user.repository.UserRepository;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CartPageController {

    private final CartService cartService;
    private final UserRepository userRepository;
    private final OrderService orderService;

    @GetMapping("/koszyk")
    public String showCartPage(Model model) {
        CartViewDto cartView = cartService.getCurrentCartView();
        model.addAttribute("items", cartView.items());
        model.addAttribute("total", cartView.subtotalPrice());
        return "cart/cart";
    }

    @GetMapping("/zamowienie")
    public String showCheckoutPage(Model model) {
        CartViewDto cartView = cartService.getCurrentCartView();

        if (cartView.items().isEmpty()) {
            log.info("Redirecting to /koszyk – empty cart");
            return "redirect:/koszyk";
        }

        model.addAttribute("cart", cartView);
        prepareUserContext(model);

        log.info("Rendering checkout page with {} items", cartView.items().size());
        return "cart/checkout";
    }

    @GetMapping("/zamowienie/potwierdzenie")
    public String showConfirmationPage(@RequestParam("orderId") @Positive Long orderId, Model model) {
        try {
            OrderConfirmationDto orderDto = orderService.getOrderForConfirmation(orderId);
            model.addAttribute("order", orderDto);
            return "cart/summary";
        } catch (Exception e) {
            log.warn("Order confirmation failed for id {}: {}", orderId, e.getMessage());
            return "redirect:/";
        }
    }

    private void prepareUserContext(Model model) {
        cartService.getCurrentUser().ifPresentOrElse(user -> {
            User userWithAddresses = userRepository.findByIdWithAddresses(user.getId())
                    .orElse(user);

            model.addAttribute("isLoggedIn", true);
            model.addAttribute("user", userWithAddresses);
            model.addAttribute("addresses", userWithAddresses.getAddresses());
        }, () -> {
            model.addAttribute("isLoggedIn", false);
            model.addAttribute("user", null);
            model.addAttribute("addresses", List.of());
        });
    }
}
