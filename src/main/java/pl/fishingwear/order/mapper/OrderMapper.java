package pl.fishingwear.order.mapper;

import org.springframework.stereotype.Component;
import pl.fishingwear.order.dto.AdminOrderDetailsDto;
import pl.fishingwear.order.dto.AdminOrderItemDto;
import pl.fishingwear.order.dto.OrderDto;
import pl.fishingwear.order.model.Order;
import pl.fishingwear.order.model.OrderItem;

import java.util.stream.Collectors;

@Component
public class OrderMapper {
    public OrderDto toDto(Order order){
        return  new OrderDto(
                order.getId(),
                order.getTotal(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }

    public AdminOrderDetailsDto toAdminDto(Order order) {
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
                .subtotal(order.getSubtotal())
                .shippingCost(order.getShippingCost())
                .total(order.getTotal())
                .shippingMethod(order.getShippingMethod())
                .items(order.getItems().stream()
                        .map(this::toItemDto)
                        .collect(Collectors.toList()))
                .build();
    }

    private AdminOrderItemDto toItemDto(OrderItem item) {
        return AdminOrderItemDto.builder()
                .productName(item.getProductName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .totalPrice(item.getTotalPrice())
                .build();
    }
}
