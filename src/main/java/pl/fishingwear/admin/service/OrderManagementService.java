package pl.fishingwear.admin.service;

import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pl.fishingwear.admin.dto.order.AdminOrderDetailsDto;
import pl.fishingwear.admin.dto.order.AdminOrderItemDto;
import pl.fishingwear.admin.dto.order.OrderAdminListDto;
import pl.fishingwear.order.model.Order;
import pl.fishingwear.order.model.OrderItem;
import pl.fishingwear.order.model.OrderStatus;
import pl.fishingwear.order.repository.OrderRepository;
import pl.fishingwear.product.model.ProductVariant;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderManagementService {

    private final OrderRepository orderRepository;

    public Page<OrderAdminListDto> getOrdersForAdmin(String searchQuery, OrderStatus statusFilter, LocalDate dateFrom, Pageable pageable) {

        Specification<Order> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(searchQuery)) {
                String likeTerm = "%" + searchQuery.toLowerCase() + "%";

                Predicate emailPredicate = cb.like(cb.lower(root.get("user").get("email")), likeTerm);
                Predicate lastNamePredicate = cb.like(cb.lower(root.get("user").get("lastName")), likeTerm);
                Predicate idPredicate = cb.like(root.get("id").as(String.class), likeTerm);
                predicates.add(cb.or(emailPredicate, lastNamePredicate, idPredicate));
            }

            if (statusFilter != null) {
                predicates.add(cb.equal(root.get("status"), statusFilter));
            }

            if (dateFrom != null) {
                predicates.add(cb.between(
                        root.get("createdAt"),
                        dateFrom.atStartOfDay(),
                        dateFrom.plusDays(1).atStartOfDay()
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Order> ordersPage = orderRepository.findAll(spec, pageable);
        return ordersPage.map(this::mapToDto);
    }

    private OrderAdminListDto mapToDto(Order order) {
        String firstName = order.getUser().getFirstName() != null ? order.getUser().getFirstName() : "";
        String lastName = order.getUser().getLastName() != null ? order.getUser().getLastName() : "";

        return new OrderAdminListDto(
                order.getId(),
                (firstName + " " + lastName).trim(),
                order.getUser().getEmail(),
                order.getCreatedAt(),
                order.getStatus(),
                order.getTotal()
        );
    }

    @Transactional
    public AdminOrderDetailsDto getOrderDetails(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono zamówienia o ID: " + orderId));

        return mapToDetailsDto(order);
    }

    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono zamówienia o ID: " + orderId));

        order.setStatus(newStatus);
        orderRepository.save(order);
    }

    private AdminOrderDetailsDto mapToDetailsDto(Order order) {
        List<AdminOrderItemDto> itemDtos = order.getItems().stream()
                .map(this::mapItemToDto)
                .collect(Collectors.toList());

        return AdminOrderDetailsDto.builder()
                .id(order.getId())
                .createdAt(order.getCreatedAt())
                .status(order.getStatus())
                .firstName(order.getFirstName())
                .lastName(order.getLastName())
                .email(order.getEmail())
                .phone(order.getPhone())
                .shippingAddress(order.getShippingAddress())
                .zipCode(order.getZipCode())
                .city(order.getCity())
                .shippingMethod(order.getShippingMethod())
                .subtotal(order.getSubtotal())
                .shippingCost(order.getShippingCost())
                .total(order.getTotal())
                .items(itemDtos)
                .build();
    }

    private AdminOrderItemDto mapItemToDto(OrderItem item) {
        ProductVariant variant = item.getProductVariant();

        String sku = "Brak SKU";
        List<String> detailsParts = new ArrayList<>();

        if (variant != null) {
            if (variant.getSku() != null) {
                sku = variant.getSku();
            }

            if (variant.getSize() != null) {
                detailsParts.add("Rozmiar: " + variant.getSize().getName());
            }
            if (variant.getColor() != null) {
                detailsParts.add("Kolor: " + variant.getColor().getName());
            }
        }

        String variantDetails = detailsParts.isEmpty() ? "Produkt podstawowy" : String.join(", ", detailsParts);

        return AdminOrderItemDto.builder()
                .productName(item.getProductName())
                .sku(sku)
                .variantDetails(variantDetails)
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .totalPrice(item.getTotalPrice())
                .build();
    }
}
