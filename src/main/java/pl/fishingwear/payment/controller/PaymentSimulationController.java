package pl.fishingwear.payment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.fishingwear.order.model.Order;
import pl.fishingwear.order.repository.OrderRepository;
import pl.fishingwear.payment.service.PaymentService;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PaymentSimulationController {

    private final OrderRepository orderRepository;
    private final PaymentService paymentService;

    @GetMapping("/zamowienie/symulacja-platnosci")
    public String showPaymentSimulator(@RequestParam("orderId") Long orderId, Model model) {
        Order order = orderRepository.findById(orderId)
                .orElse(null);

        if (order == null) {
            log.warn("Order not found for id {}", orderId);
            return "redirect:/zamowienie";
        }

        model.addAttribute("orderId", order.getId());
        model.addAttribute("totalAmount", order.getTotal());
        return "cart/payment-simulation";
    }

    public record PaymentSimulationDto(
            @NotNull Long orderId,
            @NotBlank String status
    ) {}

    @PostMapping("/api/v1/payments/webhook-symulator")
    @ResponseBody
    public ResponseEntity<?> handleSimulationWebhook(@Valid @RequestBody PaymentSimulationDto dto) {
        log.info("Received payment simulation for order {} with status {}", dto.orderId(), dto.status());

        try {
            paymentService.handlePaymentSimulation(dto);
            return ResponseEntity.ok(Map.of("message", "Status zamówienia zaktualizowany"));
        } catch (IllegalArgumentException e) {
            log.warn("Invalid simulation request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error in payment simulation", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Internal server error"));
        }
    }
}