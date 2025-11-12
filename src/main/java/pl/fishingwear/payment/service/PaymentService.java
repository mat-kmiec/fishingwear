package pl.fishingwear.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.fishingwear.payment.controller.PaymentSimulationController.PaymentSimulationDto;
import pl.fishingwear.order.exception.OrderNotFoundException;
import pl.fishingwear.order.model.Order;
import pl.fishingwear.order.repository.OrderRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    @Transactional
    public void handlePaymentSimulation(PaymentSimulationDto dto) {
        Long orderId = dto.orderId();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        if (!order.getStatus().equals("NOWE")) {
            log.warn("Otrzymano powiadomienie dla zamówienia #{} ze statusem innym niż NOWE.", orderId);
            return;
        }

        if ("COMPLETED".equals(dto.status())) {
            order.setStatus("OPŁACONE");
            log.info("Zamówienie #{} zostało automatycznie OPŁACONE (symulacja).", orderId);
            // TODO: payment

        } else if ("FAILED".equals(dto.status())) {
            order.setStatus("ANULOWANE (BŁĄD PŁATNOŚCI)");
            log.info("Zamówienie #{} zostało automatycznie ANULOWANE (symulacja).", orderId);
        }

        orderRepository.save(order);
    }
}